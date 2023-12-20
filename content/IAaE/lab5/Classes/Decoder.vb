Imports System.Threading.Tasks

Namespace Viterbi

    ''' <summary>
    ''' Декодер свёрточного кода по алгоритму Витерби.
    ''' </summary>
    Public Class Decoder
        Inherits ViterbiBase

#Region "CTOR"

        ''' <summary>
        ''' Конструктор с указанием порождающих многочленов.
        ''' </summary>
        ''' <param name="polynoms">Массив порождающих многочленов (по умолчанию в ВОСЬМЕРИЧНОЙ системе счисления).</param>
        ''' <param name="base">Основание системы счисления, в которой указаны полиномы. Обычно 8 или 10.</param>
        Public Sub New(polynoms() As Integer, Optional base As Integer = 8)
            MyBase.New(polynoms, base)
        End Sub

#End Region '/CTOR

#Region "METHODS"

        ''' <summary>
        ''' Декодирует битовый поток <paramref name="encodedMessage"/>.
        ''' </summary>
        ''' <param name="encodedMessage"></param>
        Public Function Decode(encodedMessage As IEnumerable(Of Boolean)) As Boolean()
            If (encodedMessage.Count Mod CoderBitness = 0) Then
                Dim holder As New PathHolder(StatesCount)
                'Посимвольно вычитываем из закодированного потока и декодируем:
                For symbol As Integer = 0 To encodedMessage.Count - CoderBitness Step CoderBitness
                    Dim encoderOutput(CoderBitness - 1) As Boolean
                    For bit As Integer = 0 To CoderBitness - 1
                        encoderOutput(bit) = encodedMessage(symbol + bit)
                    Next
                    UpdatePaths(encoderOutput, holder)
                Next
                Dim decodedMessage As Boolean() = holder.ChooseFinalMessage()
                Return decodedMessage
            End If
            Throw New ArgumentException($"Число бит в сообщении ({encodedMessage.Count}) не согласуется с параметрами декодера ({Me.ToString()}).")
        End Function

        ''' <summary>
        ''' Декодирует битовый поток <paramref name="encodedMessage"/>.
        ''' </summary>
        ''' <param name="encodedMessage"></param>
        Public Function Decode(encodedMessage As BitArray) As Boolean()
            Dim bools As New List(Of Boolean)
            For Each b As Boolean In encodedMessage
                bools.Add(b)
            Next
            Return Decode(bools)
        End Function

        ''' <summary>
        ''' Декодирует закодированную строку, состоящую из "1" и "0".
        ''' </summary>
        ''' <param name="binaryString">Строка из битов "1" и "0".</param>
        Public Function Decode(binaryString As String) As List(Of Boolean)
            Dim resBits As New List(Of Boolean)
            For i As Integer = 0 To binaryString.Length - CoderBitness Step CoderBitness
                Dim symbol(CoderBitness - 1) As Boolean
                For j As Integer = 0 To CoderBitness - 1
                    symbol(j) = Integer.Parse(binaryString(i + j)) > 0
                Next
                Dim decoded As Boolean() = Decode(symbol)
                resBits.AddRange(decoded)
            Next
            Return resBits
        End Function

        ''' <summary>
        ''' Декодирует битовый поток, прочитанный из текстового файла битового потока.
        ''' </summary>
        ''' <param name="streamToDecode"></param>
        ''' <param name="decodedOutputStream"></param>
        Public Sub Decode(streamToDecode As IO.TextReader, decodedOutputStream As IO.BinaryWriter)
            Dim byteSymbolLength As Integer = CoderBitness * 8
            Dim ar(0) As Byte
            Do While (streamToDecode.Peek <> -1)

                'Вычитываем по одному закодированному байту (в битах длина закодированного байта = 8 * разрядность_кодера):
                Dim encodedByte(byteSymbolLength - 1) As Boolean
                For i As Integer = 0 To byteSymbolLength - 1
                    Dim ch As Integer = streamToDecode.Read()
                    Dim bitc As Char = ChrW(ch)
                    Dim bit As Integer = Integer.Parse(bitc)
                    encodedByte(i) = CBool(bit)
                Next

                Dim decodedByte As Boolean() = Decode(encodedByte)
                Dim ba As New BitArray(decodedByte)
                ba.CopyTo(ar, 0)
                Dim b As Byte = ar(0)
                decodedOutputStream.Write(b)
            Loop
        End Sub

#End Region '/METHODS

#Region "CLOSED METHODS"

        ''' <summary>
        ''' Находит очередные выжившие пути декодера, отбрасывает невыжившие.
        ''' </summary>
        ''' <param name="encoderOut">Очередной символ закодированного сообщения на выходе кодера.</param>
        ''' <param name="paths">Вычислитель и селектор путей декодера. Обновляемое значение.</param>
        Private Sub UpdatePaths(encoderOut As Boolean(), ByRef paths As PathHolder)
            Dim currentPaths As Transition() = CalculatePathMetrics(encoderOut, paths)
            Dim survivedPaths As Transition() = SelectSurvivedPaths(currentPaths)
            paths.AddSurvivorPath(survivedPaths)
        End Sub

        ''' <summary>
        ''' Рассчитывает метрики для всех путей.
        ''' </summary>
        ''' <param name="encoderOut">Очередной символ закодированного сообщения на выходе кодера.</param>
        ''' <param name="paths">Вычислитель и селектор путей декодера.</param>
        Private Function CalculatePathMetrics(encoderOut As Boolean(), paths As PathHolder) As Transition()
            Dim transitionsWithMetrics(StatesCount - 1) As Transition
            For i As Integer = 0 To StatesCount - 1
                Dim numTransitions As Integer = Transitions(i).Count()
                For j As Integer = 0 To numTransitions - 1
                    Dim trans As Transition = Transitions(i)(j)
                    Dim branchMetric As Integer = CalculateHammingDistance(trans.CoderOutBits, encoderOut)
                    Dim pathMetric As Integer = paths.GetPathMetricByNode(trans.SourceNode)
                    Dim t As Transition = trans.Clone()
                    t.Metrics = branchMetric + pathMetric
                    transitionsWithMetrics(i * numTransitions + j) = t
                Next
            Next
            Return transitionsWithMetrics
        End Function

        ''' <summary>
        ''' Выбирает и возвращает выжившие пути.
        ''' </summary>
        ''' <remarks>
        ''' Выбирает из 2-х конкурирующих путей, входящих в один узел, путь с минимальной метрикой.
        ''' Также здесь можно просмотреть невыживший путь, если это для чего-то нужно.
        ''' </remarks>
        Private Function SelectSurvivedPaths(currentPaths As Transition()) As Transition()
            Dim lu As ILookup(Of Integer, Transition) = currentPaths.ToLookup(Of Integer, Transition)(
                    Function(trans) trans.DestinationNode,
                    Function(trans) trans
                    )
            Dim survivors(TransitionsCount - 1) As Transition
            For i As Integer = 0 To TransitionsCount - 1
                Dim minMetricTransition As Transition = lu(i)(0)
                For Each t As Transition In lu(i)
                    If (t.Metrics < minMetricTransition.Metrics) Then
                        minMetricTransition = t
                    End If
                Next
                survivors(i) = minMetricTransition
            Next
            Return survivors
        End Function

        ''' <summary>
        ''' Рассчитывает Хэмингово расстояние между несколькими битовыми комбинациями.
        ''' </summary>
        ''' <param name="bitCombination">Набор битовых комбинаций. Например, {0,1} и {1,1}. Разрядности комбинаций должны быть одинаковыми.</param>
        ''' <remarks>
        ''' Хэмингово расстояние рассчитывается следующим образом: 
        ''' битовые комбинации складывюется (ПОРАЗРЯДНО) по модулю два и далее рассчитывается количество единиц в получившемся значении. 
        ''' Например, хэмингово расстояние для двух комбинаций 01 и 00 рассчитывается следующим образом: 
        ''' количество единиц (0 xor 0; 1 xor 0) = количество единиц (0;1) = 1.
        ''' </remarks>
        Private Shared Function CalculateHammingDistance(ParamArray bitCombination() As IEnumerable(Of Boolean)) As Integer
            Dim sumOfOnes As Integer = 0
            For bit As Integer = 0 To bitCombination(0).Count - 1 'поразрядно 
                Dim resultBit As Boolean = False
                For comb As Integer = 0 To bitCombination.Count - 1 'для всех комбинаций
                    resultBit = resultBit Xor bitCombination(comb)(bit) 'считаем сумму по модулю 2
                Next
                If resultBit Then 'если 1, то инкрементируем сумму
                    sumOfOnes += 1
                End If
            Next
            Return sumOfOnes
        End Function

#End Region '/CLOSED METHODS

#Region "NESTED TYPES"

        ''' <summary>
        ''' Содержит полный набор выживших и невыживших путей декодера.
        ''' </summary>
        Private Class PathHolder

            ''' <summary>
            ''' Создаёт объект для хранения путей при декодировании.
            ''' </summary>
            ''' <param name="decoderStatesCount">Число состояний декодера. Число выживших и невыживших путей будет в 2 раза меньше.</param>
            Public Sub New(decoderStatesCount As Integer)
                Dim count As Integer = decoderStatesCount \ 2
                ReDim Paths(count - 1)
                For i As Integer = 0 To count - 1
                    Paths(i) = New DecoderPath()
                Next
            End Sub

#Region "PROPS"

            ''' <summary>
            ''' Массив путей декодера.
            ''' </summary>
            Public ReadOnly Property Paths As DecoderPath()

            ''' <summary>
            ''' Путь уже имеет узлы или ещё нет (флаги для выжившей и невыжевшей ветвей).
            ''' </summary>
            Private IsFirstNode As Boolean = True

#End Region '/PROPS

#Region "METHODS"

            ''' <summary>
            ''' Восстанавливает сообщение, выбирая его из финальных выживших путей кодера.
            ''' </summary>
            Public Function ChooseFinalMessage() As Boolean()
                Dim finalPaths As IEnumerable(Of DecoderPath) = From p As DecoderPath In Me.Paths
                                                                Where (p.Count > 0)
                                                                Select p Order By p.Metrics 'путь с минимальной метрикой окажется вверху списка
                For Each path In finalPaths
                    Dim decoded(path.Count - 1) As Boolean
                    For i As Integer = 0 To path.Count - 1
                        decoded(i) = path.SurvivorPath(i).DestinationNodeMsb
                    Next
                    Return decoded 'Если имеется несколько путей с одинаковой минимальной метрикой, выбирается первый, потом выходим из цикла.
                Next
                Return {}
            End Function

            ''' <summary>
            ''' Добавляет выживший путь в <see cref="Paths"/>.
            ''' </summary>
            ''' <param name="survivors">Массив выживших переходов.</param>
            Public Sub AddSurvivorPath(ByRef survivors As Transition())
                If IsFirstNode Then
                    AddSurvivorPathFirstly(survivors)
                Else
                    'Новый путь продолжает предыдущий => его начальный узел должен выходить из конечного узла предыдущего узла.
                    'Запоминаем соотвтествие индексов новых выживших уже существующим путям
                    '(если сразу вставлять элемент пути, коллекция Path изменялась бы, что привело бы к ошибкам):
                    Dim survivorIndeces(survivors.Length - 1) As Integer
                    Dim prevPathIndeces(survivors.Length - 1) As Integer
                    For i As Integer = 0 To survivors.Length - 1
                        survivorIndeces(i) = i
                        For j As Integer = 0 To Paths.Count - 1
                            If (Paths(j).DestinationNode = survivors(i).SourceNode) Then
                                prevPathIndeces(i) = j
                                Exit For 'нет нужды идти дальше
                            End If
                        Next
                    Next

                    'Ключ - индекс текущего пути в Path, значения - новые выжившие пути:
                    Dim pathIndeces As ILookup(Of Integer, Integer) = survivorIndeces.ToLookup(Of Integer, Integer)(
                            Function(key) prevPathIndeces(key),
                            Function(key) key
                            )

                    'Запомним индексы путей, которые можно удалить:
                    Dim survivorsToRemove As New Queue(Of Integer)
                    For i As Integer = 0 To survivorIndeces.Length - 1
                        If (Not pathIndeces.Contains(i)) Then
                            survivorsToRemove.Enqueue(i)
                        End If
                    Next

                    'По сохранённым индексам добавим выжившие пути в Path:
                    For Each survIndeces In pathIndeces
                        Dim pathIndex As Integer = survIndeces.Key

                        'Если по ключу находится 2 узла, 2-ой вставляем по индексу невыжившего пути
                        If (survIndeces.Count > 1) Then
                            Dim survivorToRemove As Integer = survivorsToRemove.Dequeue()
                            Paths(survivorToRemove).SurvivorPath.Clear() 'удаляем невыживший путь
                            For Each node In Paths(pathIndex).SurvivorPath 'копируем текущий путь вместо удалённого
                                Paths(survivorToRemove).AddSurvivorNode(node)
                            Next
                            Paths(survivorToRemove).AddSurvivorNode(survivors(survIndeces(1)))
                        End If

                        '1-ый элемент добавляем как обычно:
                        If (survIndeces.Count >= 1) Then
                            Paths(pathIndex).AddSurvivorNode(survivors(survIndeces(0)))
                        End If
                    Next
                End If
            End Sub

            ''' <summary>
            ''' Первые узлы добавляем без привязки к предыдущим.
            ''' </summary>
            ''' <param name="survivors"></param>
            Private Sub AddSurvivorPathFirstly(ByRef survivors As Transition())
                For i As Integer = 0 To survivors.Length - 1
                    Paths(i).AddSurvivorNode(survivors(i))
                Next
                IsFirstNode = False
            End Sub

            ''' <summary>
            ''' Возвращает метрику пути по узлу назначения.
            ''' </summary>
            ''' <param name="node">Узел назначения, для пути которого нужна метрика.</param>
            Public Function GetPathMetricByNode(node As Integer) As Integer
                If (Not IsFirstNode) Then 'т.е. в пути уже имеются узлы
                    For Each path As DecoderPath In Paths
                        Dim count As Integer = path.SurvivorPath.Count() 'так быстрее, чем (path.DestinationNode = node)
                        If (path.SurvivorPath(count - 1).DestinationNode = node) Then
                            Return path.Metrics
                        End If
                    Next
                End If
                Return 0
            End Function

#End Region '/METHODS

        End Class '/PathHolder

        ''' <summary>
        ''' Полный путь декодирования.
        ''' </summary>
        Private Class DecoderPath

#Region "PROPS"

            ''' <summary>
            ''' Список выживших переходов при декодировании.
            ''' </summary>
            Public ReadOnly Property SurvivorPath As New List(Of Transition)

            ''' <summary>
            ''' Метрика последнего перехода всего выжившего пути.
            ''' </summary>
            Public ReadOnly Property Metrics As Integer
                Get
                    Return SurvivorPath(SurvivorPath.Count - 1).Metrics
                End Get
            End Property

            ''' <summary>
            ''' Узел назначения последнего перехода всего выжившего пути.
            ''' </summary>
            Public ReadOnly Property DestinationNode As Integer
                Get
                    Return SurvivorPath(SurvivorPath.Count - 1).DestinationNode
                End Get
            End Property

            ''' <summary>
            ''' Число узлов в выжившем пути.
            ''' </summary>
            Public ReadOnly Property Count As Integer
                Get
                    Return SurvivorPath.Count()
                End Get
            End Property

#End Region '/PROPS

#Region "METHODS"

            ''' <summary>
            ''' Добавляет узел к выжившему пути.
            ''' </summary>
            ''' <param name="node"></param>
            Public Sub AddSurvivorNode(node As Transition)
                SurvivorPath.Add(node)
            End Sub

            ''' <summary>
            ''' Возвращает список переходов. В конце - метрика пути.
            ''' </summary>
            Public Overloads Function ToString() As String
                Dim sb As New Text.StringBuilder()
                If (SurvivorPath.Count > 0) Then
                    sb.Append(Convert.ToString(SurvivorPath(0).SourceNode, 2).PadLeft(3, "0"c))
                    sb.Append("; ")
                    For Each t As Transition In SurvivorPath
                        sb.Append(Convert.ToString(t.DestinationNode, 2).PadLeft(3, "0"c))
                        sb.Append("; ")
                    Next
                    sb = sb.Remove(sb.Length - 2, 2)
                End If
                sb.Append($" ({Metrics})")
                Return sb.ToString()
            End Function

#End Region '/METHODS

        End Class '/DecoderPath

#End Region '/NESTED TYPES

    End Class '/Decoder

End Namespace