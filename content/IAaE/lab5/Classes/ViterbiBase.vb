Namespace Viterbi

    ''' <summary>
    ''' Базовый класс для кодера и декодера.
    ''' </summary>
    ''' 

    Public MustInherit Class ViterbiBase

#Region "CTOR"

        ''' <summary>
        ''' Конструктор с указанием порождающих многочленов.
        ''' </summary>
        ''' <param name="polynoms">Массив порождающих многочленов (по умолчанию в ВОСЬМЕРИЧНОЙ системе счисления).</param>
        ''' <param name="base">Основание системы счисления, в которой указаны полиномы. Обычно 8 или 10.</param>
        Public Sub New(polynoms() As Integer, base As Integer)
            Select Case base
                Case 10
                    Me.Polynoms = polynoms
                Case Else
                    ReDim Me.Polynoms(polynoms.Length - 1)
                    For i As Integer = 0 To polynoms.Length - 1
                        Me.Polynoms(i) = ConvertFromTo(polynoms(i), base, 10)
                    Next
            End Select
        End Sub

#End Region '/CTOR

#Region "PROPS"

        ''' <summary>
        ''' Массив порождающих многочленов.
        ''' </summary>
        Public ReadOnly Property Polynoms As Integer()

        ''' <summary>
        ''' Разрядность кодера.
        ''' </summary>
        ''' <remarks>Соответствует числу полиномов.</remarks>
        Public ReadOnly Property CoderBitness As Integer
            Get
                Return Polynoms.Length
            End Get
        End Property

        ''' <summary>
        ''' Разрядность сдвигового регистра кодера.
        ''' </summary>
        Public ReadOnly Property RegisterLength As Integer
            Get
                If (_RegisterLength = -1) Then
                    Dim maxPolynom As Integer = Polynoms.Max() 'определяем разрядность регистра по наибольшему числу
                    Dim tmp As Integer = ConvertFromTo(maxPolynom, 10, 2, _RegisterLength) 'для определения разрядности регистра
                End If
                Return _RegisterLength
            End Get
        End Property
        Private _RegisterLength As Integer = -1

        ''' <summary>
        ''' Таблица возможных состояний кодера.
        ''' </summary>
        Public ReadOnly Property CoderStates As Dictionary(Of Integer, Integer)
            Get
                If (_CoderStates.Count = 0) Then
                    _CoderStates = CalculateCoderStates()
                End If
                Return _CoderStates
            End Get
        End Property
        Private _CoderStates As New Dictionary(Of Integer, Integer)

        ''' <summary>
        ''' Таблица переходов кодера.
        ''' </summary>
        ''' <remarks>
        ''' Пользуясь решёткой, легко проводить кодирование, не высчитывая каждый раз суммы по модулю два. 
        ''' Для этого нужно взять за исходное состояние узел 000 и далее перемещаться по стрелкам в один из двух узлов. 
        ''' Выбор следующего узла определяется кодируемым битом данных – если бит данных «1», то первый слева бит следующего узла должен быть «1», 
        ''' если кодируемый бит – «0», то первый слева бит следующего узла должен быть «0». 
        ''' Физически переход соответствует сдвигу в регистре кодера. 
        ''' Выходным значением кодера будет (<see cref="Transition.CoderOut"/>).
        ''' </remarks>
        Public ReadOnly Property Transitions As Lookup(Of Integer, Transition)
            Get
                If (_Transitions Is Nothing) Then
                    _Transitions = CalculateTransitionsGrid()
                End If
                Return _Transitions
            End Get
        End Property
        Private _Transitions As Lookup(Of Integer, Transition) = Nothing

        ''' <summary>
        ''' Число возможных состояний регистра.
        ''' </summary>
        ''' <remarks>Всего 2^k ветвей (число заваисит только от длины регистра).</remarks>
        Protected ReadOnly Property StatesCount As Integer
            Get
                If (_StatesCount = 0) Then
                    _StatesCount = CInt(Math.Pow(2, RegisterLength))
                End If
                Return _StatesCount
            End Get
        End Property
        Private _StatesCount As Integer = 0

        ''' <summary>
        ''' Число возможных переходов из узла в узел / число узлов в решётке переходов
        ''' </summary>
        ''' <remarks>Всего 2^(k-1) переходов (число заваисит только от длины регистра).</remarks>
        Protected ReadOnly Property TransitionsCount As Integer
            Get
                If (_TransitionNodesCount = 0) Then
                    _TransitionNodesCount = CInt(Math.Pow(2, RegisterLength - 1))
                End If
                Return _TransitionNodesCount
            End Get
        End Property
        Private _TransitionNodesCount As Integer = 0

#End Region '/PROPS

#Region "METHODS"

        ''' <summary>
        ''' Возвращает список образующих многочленов в восьмеричной форме.
        ''' </summary>
        Public Overloads Function ToString() As String
            Dim sb As New Text.StringBuilder()
            sb.Append("(")
            For Each p As Integer In Polynoms
                sb.Append($"{p.ToOctal()},")
            Next
            sb = sb.Remove(sb.Length - 1, 1)
            sb.Append(")")
            Return sb.ToString()
        End Function

#End Region '/METHODS

#Region "CLOSED METHODS"

        ''' <summary>
        ''' Рассчитывает таблицу всех возможных состояний кодера.
        ''' </summary>
        ''' <remarks>Значение состояния определяется по его индексу.</remarks>
        Private Function CalculateCoderStates() As Dictionary(Of Integer, Integer)
            Dim states As New Dictionary(Of Integer, Integer) 'ключ словаря = номер состояния, значение = само состояние
            states.Add(0, 0) 'исходное состояние

            For stateNumber As Integer = 1 To StatesCount - 1 'начинаем с 1-го, т.к. 0-ое добавили выше
                Dim adderOuts As New List(Of Boolean)(Polynoms.Length) 'возможные значения выхода - по количеству сумматоров (или полиномов)
                For Each polynom As Integer In Polynoms 'рассчитываем каждое значение выхода: XORим между собой те биты содержимого регистра, на которые указывают "1" полинома
                    Dim adderOut As Boolean = False 'состояние выхода сумматора
                    Dim register As New BitArray({stateNumber}) 'содержимое регистра кодера = значение номера состояния
                    For i As Integer = 0 To RegisterLength - 1 'идём по всем битам регистра кодера
                        Dim polynomBit As Boolean = (((polynom >> i) And 1) > 0) 'i-ый бит полинома
                        If polynomBit Then 'если бит "1", то XORим значение i-го бита регистра с предыдущим значением выхода сумматора
                            adderOut = adderOut Xor register(i)
                        End If
                    Next
                    adderOuts.Add(adderOut)
                Next

                'Объединяем все выходы в одно состояние:
                Dim state As Integer = 0
                For Each adderOut In adderOuts
                    Dim currentBit As Integer = (CInt(adderOut) And 1)
                    state = (state << 1) Or currentBit
                Next

                states.Add(stateNumber, state)
            Next
            Return states
        End Function

        ''' <summary>
        ''' Рассчитывает решётку переходов.
        ''' </summary>
        Private Function CalculateTransitionsGrid() As Lookup(Of Integer, Transition)
            Dim transitions As New List(Of Transition)(TransitionsCount)
            For node As Integer = 0 To TransitionsCount - 1 'все числа от 0 до 2^(k-1)-1 - это значения узлов
                For mostSignificantBit As Integer = 0 To 1
                    Dim reg As Integer = node Or (mostSignificantBit << (RegisterLength - 1)) 'старший бит заменяем "0" или "1"
                    Dim coderOut As Integer = CoderStates(reg)
                    Dim destinationNode As Integer = reg >> 1
                    transitions.Add(New Transition(RegisterLength, Polynoms.Count, node, destinationNode, coderOut))
                Next
            Next
            Dim ilu As ILookup(Of Integer, Transition) = transitions.ToLookup(Of Integer, Transition)(
                Function(trans) trans.SourceNode, 'ключ - значение стартового узла
                Function(trans) trans 'значение - параметры (узел назначения, выход кодера)
                )
            Dim lu As Lookup(Of Integer, Transition) = CType(ilu, Lookup(Of Integer, Transition))
            Return lu
        End Function

        ''' <summary>
        ''' Выбирает узел решётки, в который нужно идти при кодировании бита <paramref name="bitToEncode"/>.
        ''' </summary>
        ''' <param name="bitToEncode">Очередной кодируемый бит сообщения.</param>
        ''' <param name="currentTransitions">Массив возможных переходов из текущего узла.</param>
        Protected Function GetNextTransition(bitToEncode As Boolean, currentTransitions As IEnumerable(Of Transition)) As Transition
            For Each t In currentTransitions
                Select Case bitToEncode
                    Case True 'если бит "1", то переход в узел со старшим битом "1"
                        If t.DestinationNodeMsb Then
                            Return t
                        End If
                    Case False 'если бит "0", то переход в узел со старшим битом "0"
                        If (Not t.DestinationNodeMsb) Then
                            Return t
                        End If
                End Select
            Next
            Return Nothing
        End Function

#End Region '/CLOSED METHODS

    End Class '/ViterbiBase

    ''' <summary>
    ''' Параметры перехода кодера.
    ''' </summary>
    Public Class Transition

#Region "CTOR"

        ''' <summary>
        ''' 
        ''' </summary>
        ''' <param name="registerBitness">Разрядность регистра.</param>
        ''' <param name="coderBitness">Разрядность кодера. Совпадает с числом порождающих многочленов кодера.</param>
        ''' <param name="srcNode">Исходящий узел.</param>
        ''' <param name="destNode">Входящий узел.</param>
        ''' <param name="coderOut">Значение на выходе кодера.</param>
        Public Sub New(registerBitness As Integer, coderBitness As Integer, srcNode As Integer, destNode As Integer, coderOut As Integer)
            Me.RegisterBitness = registerBitness
            Me.CoderBitness = coderBitness
            Me.SourceNode = srcNode
            Me.DestinationNode = destNode
            Me.CoderOut = coderOut
        End Sub

#End Region '/CTOR

#Region "PROPS"

        ''' <summary>
        ''' Значение узла, из которого осуществляется переход.
        ''' </summary>
        Public ReadOnly Property SourceNode As Integer

        ''' <summary>
        ''' Значение узла, в который возможен переход.
        ''' </summary>
        Public ReadOnly Property DestinationNode As Integer

        ''' <summary>
        ''' Выход кодера в виде битов.
        ''' </summary>
        Public ReadOnly Property CoderOutBits As Boolean()
            Get
                If (_CoderOutBits.Length = 0) Then
                    ReDim _CoderOutBits(CoderBitness - 1)
                    For i As Integer = 0 To CoderBitness - 1
                        Dim shift As Integer = CoderBitness - 1 - i
                        _CoderOutBits(i) = CBool((CoderOut >> shift) And 1)
                    Next
                End If
                Return _CoderOutBits
            End Get
        End Property
        Private _CoderOutBits() As Boolean = {}

        ''' <summary>
        ''' Старший бит узла назначения.
        ''' </summary>
        ''' <remarks>Используется при принятии решения о переходе.</remarks>
        Public ReadOnly Property DestinationNodeMsb As Boolean
            Get
                If (_DestinationNodeMsb Is Nothing) Then
                    Dim firstBit As Integer = DestinationNode >> (RegisterBitness - 2)
                    _DestinationNodeMsb = (firstBit > 0)
                End If
                Return _DestinationNodeMsb.Value
            End Get
        End Property
        Private _DestinationNodeMsb As Boolean? = Nothing

        ''' <summary>
        ''' Метрика пути.
        ''' </summary>
        ''' <remarks>Используется декодером.</remarks>
        Public Property Metrics As Integer = 0

#End Region '/PROPS

#Region "FIELDS"

        ''' <summary>
        ''' Разрядность кодера. Соответствует числу образующих многочленов.
        ''' </summary>
        Private ReadOnly CoderBitness As Integer

        ''' <summary>
        ''' Разрядность регистра.
        ''' </summary>
        Private ReadOnly RegisterBitness As Integer

        ''' <summary>
        ''' Значение на выходе кодера на данном переходе.
        ''' </summary>
        Private ReadOnly CoderOut As Integer

#End Region '/FIELDS

#Region "METHODS"

        ''' <summary>
        ''' Клонирует переход.
        ''' </summary>
        Public Function Clone() As Transition
            Dim cloned As Transition = CType(Me.MemberwiseClone(), Transition)
            cloned.Metrics = Me.Metrics
            Return cloned
        End Function

        Public Overloads Function ToString() As String
            Dim sb As New Text.StringBuilder()
            sb.Append(Convert.ToString(SourceNode, 2).PadLeft(CoderBitness + 1, "0"c))
            sb.Append("; ")
            sb.Append(Convert.ToString(DestinationNode, 2).PadLeft(CoderBitness + 1, "0"c))
            sb.Append("; ")
            sb.Append($"({Metrics})")
            Return sb.ToString()
        End Function

#End Region '/METHODS

    End Class '/Transition

    ''' <summary>
    ''' Вспомогательные методы.
    ''' </summary>
    <Runtime.CompilerServices.Extension()>
    Public Module Helpers

        ''' <summary>
        ''' Преобразует число из десятичной системы счисления в число в двоичной системе счисления.
        ''' </summary>
        <Runtime.CompilerServices.Extension()>
        Public Function ToBinary(dec As Integer) As Integer
            Return ConvertFromTo(dec, 10, 2)
        End Function

        ''' <summary>
        ''' Преобразует число из десятичной системы счисления в число в восьмеричной системе счисления.
        ''' </summary>
        <Runtime.CompilerServices.Extension()>
        Public Function ToOctal(dec As Integer) As Integer
            Return ConvertFromTo(dec, 10, 8)
        End Function

        ''' <summary>
        ''' Преобразует число из восьмеричной системы счисления в число в десятичной системе счисления.
        ''' </summary>
        <Runtime.CompilerServices.Extension()>
        Public Function ToDecimal(oct As Integer) As Integer
            Return ConvertFromTo(oct, 8, 10)
        End Function

        ''' <summary>
        ''' Преобразует число из одной системы счисления в другую.
        ''' </summary>
        ''' <param name="number">Число в исходной системе счисления.</param>
        ''' <param name="fromBase">Исходная система счисления.</param>
        ''' <param name="toBase">Целевая система счисления.</param>
        ''' <param name="bitness">Выходное значение - разрядность в целевой системе счисления. Если не требуется, можно не передавать.</param>
        ''' <remarks>
        ''' Будьте внимательны: при переводе в двоичную систему может возникнуть ошибка переполнения, т.к. двоичные числа очень длинные.
        ''' Перевод в 16-ную систему и обратно не обрабатывается.
        ''' </remarks>
        Public Function ConvertFromTo(number As Integer, fromBase As Integer, toBase As Integer, Optional ByRef bitness As Integer = 0) As Integer
            Dim result As Integer = 0
            Dim i As Integer = 0
            Do While (number <> 0)
                result += (number Mod toBase) * CInt(Math.Pow(fromBase, i))
                number = CInt(Math.Truncate(number / toBase))
                i += 1
            Loop
            bitness = i
            Return result
        End Function

        ''' <summary>
        ''' Возвращает строку, представляющую двоичную последовательность переданной строки.
        ''' </summary>
        ''' <param name="s">Произвольная строка.</param>
        <Runtime.CompilerServices.Extension()>
        Public Function ToBinString(s As String) As String
            Dim messageBytes As Byte() = Text.Encoding.UTF8.GetBytes(s)
            Dim ba As New BitArray(messageBytes)
            Dim sb As New Text.StringBuilder()
            For Each b As Boolean In ba
                sb.Append(CInt(b) And 1)
            Next
            Return sb.ToString()
        End Function

        ''' <summary>
        ''' Возвращает строку, представляющую двоичную последовательность переданного массива битов.
        ''' </summary>
        ''' <param name="bits">Произвольная битовая последовательность.</param>
        <Runtime.CompilerServices.Extension()>
        Public Function ToBinString(bits As IEnumerable(Of Boolean)) As String
            Dim sb As New Text.StringBuilder()
            For Each b As Boolean In bits
                sb.Append(CInt(b) And 1)
            Next
            Return sb.ToString()
        End Function

        ''' <summary>
        ''' Преобразует строку битов в битовую последовательность.
        ''' </summary>
        ''' <param name="binaryString">Строка, составленная из "1" и "0".</param>
        <Runtime.CompilerServices.Extension()>
        Public Function FromBinString(binaryString As String) As List(Of Boolean)
            Dim bits As New List(Of Boolean)
            For Each c As Char In binaryString.ToCharArray()
                bits.Add(Boolean.Parse(c))
            Next
            Return bits
        End Function

        ''' <summary>
        ''' Преобразует битовую последовательность в строку ASCII.
        ''' </summary>
        <Runtime.CompilerServices.Extension()>
        Public Function ToUtf8String(bits As IEnumerable(Of Boolean)) As String
            Dim bytes As New List(Of Byte)
            For i As Integer = 0 To bits.Count - 8 Step 8
                Dim int As Integer = 0
                For j As Integer = 0 To 7
                    int = int Or ((CInt(bits(i + j)) And 1) << j)
                Next
                bytes.Add(CByte(int))
            Next
            Dim s As String = Text.Encoding.UTF8.GetString(bytes.ToArray())
            Return s
        End Function

        ''' <summary>
        ''' Побитово сравнивает 2 массива.
        ''' </summary>
        <Runtime.CompilerServices.Extension()>
        Public Function BinaryEquals(ar1 As IEnumerable(Of Boolean), ar2 As IEnumerable(Of Boolean)) As Boolean
            If (ar1.Count <> ar2.Count) Then
                Return False
            End If
            For i As Integer = 0 To ar1.Count - 1
                If (ar1(i) <> ar2(i)) Then
                    Return False
                End If
            Next
            Return True
        End Function

        ''' <summary>
        ''' Добавляет в битовый поток <paramref name="message"/> заданный процент случайных ошибок.
        ''' </summary>
        ''' <param name="message">Исходное сообщение.</param>
        ''' <param name="errorsPercent">Процент ошибок, от 0 до 100.</param>
        Public Function GetMessageWithErrors(message As IEnumerable(Of Boolean), errorsPercent As Integer) As List(Of Boolean)
            Dim numErrors As Integer = CInt(Math.Truncate(message.Count * errorsPercent / 100))
            Dim errorsIndeces As New List(Of Integer)
            Dim rnd As New Random()
            For i As Integer = 0 To numErrors - 1
                Dim r As Integer = rnd.Next(0, message.Count)
                errorsIndeces.Add(r)
            Next
            Dim messageWithErrors As New List(Of Boolean)
            For i As Integer = 0 To message.Count - 1
                If errorsIndeces.Contains(i) Then
                    messageWithErrors.Add(Not message(i))
                Else
                    messageWithErrors.Add(message(i))
                End If
            Next
            Return messageWithErrors
        End Function

    End Module '/Helpers

End Namespace