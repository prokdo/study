Namespace Viterbi

    ''' <summary>
    ''' Кодер свёрточного кода.
    ''' </summary>
    Public Class Encoder
        Inherits ViterbiBase

#Region "CTOR"

        ''' <summary>
        ''' Конструктор кодера свёрточного кода с указанием порождающих многочленов.
        ''' </summary>
        ''' <param name="polynoms">Массив порождающих многочленов (по умолчанию в ВОСЬМЕРИЧНОЙ системе счисления).</param>
        ''' <param name="base">Основание системы счисления, в которой указаны полиномы. Обычно 8 или 10.</param>
        Public Sub New(polynoms() As Integer, Optional base As Integer = 8)
            MyBase.New(polynoms, base)
        End Sub

#End Region '/CTOR

#Region "METHODS"

        ''' <summary>
        ''' Кодирует сообщение <paramref name="message"/>, заданное массивом битов.
        ''' </summary>
        ''' <remarks>
        ''' Пользуясь решёткой, легко проводить кодирование, не высчитывая каждый раз суммы по модулю два. 
        ''' Для этого нужно взять за исходное состояние узел 000 и далее перемещаться по стрелкам в один из двух узлов. 
        ''' Выбор следующего узла определяется кодируемым битом данных: если бит данных «1», то первый слева бит следующего узла должен быть «1», 
        ''' если кодируемый бит «0», то первый слева бит следующего узла должен быть «0». 
        ''' Физически операция перемещения по стрелке соответствует сдвигу в регистре кодера. 
        ''' Выходным значением кодера будут цифры, соответствующие стрелке.
        ''' </remarks>
        Public Function Encode(message As BitArray) As List(Of Boolean)
            Dim encodedMessage As New List(Of Boolean)
            Dim currentNode As Integer = 0 'начинаем с узла "0"
            For stepNum As Integer = 0 To message.Length - 1
                Dim nextTransition As Transition = GetNextTransition(message(stepNum), Transitions(currentNode))
                currentNode = nextTransition.DestinationNode
                encodedMessage.AddRange(nextTransition.CoderOutBits)
            Next
            Return encodedMessage
        End Function

        ''' <summary>
        ''' Кодирует сообщение, заданное строкой символов.
        ''' </summary>
        ''' <param name="message">Сообщение в виде текстовой строки.</param>
        ''' <remarks>Для преобразования в байты применяется кодировка UTF8.</remarks>
        Public Function Encode(message As String) As List(Of Boolean)
            Dim messageBytes As Byte() = Text.Encoding.UTF8.GetBytes(message)
            Dim ba As New BitArray(messageBytes)
            Dim encoded As List(Of Boolean) = Encode(ba)
            Return encoded
        End Function

        ''' <summary>
        ''' Кодирует поток <paramref name="streamToEncode"/> и сохраняет в поток <paramref name="encodedOutputStream"/>.
        ''' </summary>
        ''' <param name="streamToEncode">Двоичный поток, который будет побайтово прочитан и закодирован по алгоритму Витерби.</param>
        ''' <param name="encodedOutputStream">В этот поток (в текстовом!) виде будет сохранён результат.</param>
        Public Sub Encode(streamToEncode As IO.BinaryReader, encodedOutputStream As IO.StreamWriter)
            Do While (streamToEncode.BaseStream.Position < streamToEncode.BaseStream.Length)
                Dim b As Byte = streamToEncode.ReadByte()
                Dim ba As New BitArray({b})
                Dim encodedBits As IEnumerable(Of Boolean) = Encode(ba)
                For Each bit As Boolean In encodedBits
                    encodedOutputStream.Write(CInt(bit) And 1)
                Next
            Loop
        End Sub

        ''' <summary>
        ''' Кодирует сообщение <paramref name="message"/>, заданное массивом битов.
        ''' </summary>
        Public Function Encode(message As IEnumerable(Of Boolean)) As List(Of Boolean)
            Dim b As New BitArray(message.ToArray())
            Return Encode(b)
        End Function

#End Region '/METHODS

    End Class '/Encoder

End Namespace