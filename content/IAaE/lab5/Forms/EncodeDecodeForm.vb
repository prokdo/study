Imports System.Runtime.Remoting.Messaging
Imports System.Text
Imports LAB5.Viterbi

Public Class EncodeDecodeForm

    Dim PercentArray() As Integer = {0, 10, 15, 25, 50, 75, 100}

    Dim IntArrayF As Integer()

    Dim MessageF As String

    Dim PercentOfErrorF As Integer

    Dim BoolKeyF As Boolean


    Public Sub New(IntArray As Integer(), Message As String, PercentOfError As Integer, BoolKey As Boolean)

        InitializeComponent()

        IntArrayF = IntArray

        MessageF = Message

        PercentOfErrorF = PercentOfError

        BoolKeyF = BoolKey

        If (PercentOfErrorF = -1) Then

            CalculateWithCycle()

        Else

            CalculateNotCycle()

        End If

    End Sub

    Private Sub CalculateWithCycle()

        Dim enc As New Viterbi.Encoder(IntArrayF)
        Dim decod As New Viterbi.Decoder(IntArrayF)

        Dim encoded As IEnumerable(Of Boolean)
        Dim restored As IEnumerable(Of Boolean)

        If BoolKeyF = True Then

            Dim BooleanMessageArray As Boolean() = MessageF.ToCharArray().Select(Function(c) c = "1"c).ToArray()

            BitMessageRichTextBox.Text += BooleanMessageArray.ToBinString() + $" ({BooleanMessageArray.ToBinString().Length} бит)"

            encoded = enc.Encode(BooleanMessageArray)

        Else

            BitMessageRichTextBox.Text += MessageF.ToBinString() + $" ({MessageF.ToBinString().Length} бит)"

            encoded = enc.Encode(MessageF)

        End If

        For Percent As Integer = 0 To PercentArray.Length - 1

            Dim errEncoded = GetMessageWithErrors(encoded, PercentArray(Percent))

            restored = decod.Decode(errEncoded)

            If Percent = 0 Then

                EncodeMessageRichTextBox.Text += $"Без ошибок ({errEncoded.ToBinString().Length} бит): {errEncoded.ToBinString()}"

                DecodeMessageRichTextBox.Text += $"Без ошибок ({restored.ToBinString().Length} бит): {restored.ToBinString()}"

                UtfMessageRichTextBox.Text += $"Исходное сообщение: {MessageF}{vbCrLf}"

                UtfMessageRichTextBox.Text += $"Декодированное сообщение: {vbCrLf}"

                If BoolKeyF = True Then

                    UtfMessageRichTextBox.Text += $"Без ошибок: {restored.ToBinString()}"

                Else

                    UtfMessageRichTextBox.Text += $"Без ошибок: {restored.ToUtf8String()}"

                End If

            Else

                EncodeMessageRichTextBox.Text += $"{PercentArray(Percent)}% ошибок: {errEncoded.ToBinString()}"

                DecodeMessageRichTextBox.Text += $"{PercentArray(Percent)}% ошибок: {restored.ToBinString()}"

                If BoolKeyF = True Then

                    UtfMessageRichTextBox.Text += $"{PercentArray(Percent)}% ошибок: {restored.ToBinString()}"

                Else

                    UtfMessageRichTextBox.Text += $"{PercentArray(Percent)}% ошибок: {restored.ToUtf8String()}"

                End If

            End If

            If Percent <> PercentArray.Length - 1 Then

                EncodeMessageRichTextBox.Text += vbCrLf
                DecodeMessageRichTextBox.Text += vbCrLf
                UtfMessageRichTextBox.Text += vbCrLf

            End If

        Next

    End Sub

    Private Sub CalculateNotCycle()

        Dim enc As New Viterbi.Encoder(IntArrayF)
        Dim decod As New Viterbi.Decoder(IntArrayF)

        Dim encoded As IEnumerable(Of Boolean)
        Dim restored As IEnumerable(Of Boolean)

        If BoolKeyF = True Then

            Dim BooleanMessageArray As Boolean() = MessageF.ToCharArray().Select(Function(c) c = "1"c).ToArray()

            BitMessageRichTextBox.Text += BooleanMessageArray.ToBinString() + $" ({BooleanMessageArray.ToBinString().Length} бит)"

            encoded = enc.Encode(BooleanMessageArray)

        Else

            BitMessageRichTextBox.Text += MessageF.ToBinString() + $" ({MessageF.ToBinString().Length} бит)"

            encoded = enc.Encode(MessageF)

        End If

        EncodeMessageRichTextBox.Text += encoded.ToBinString()

        If PercentOfErrorF = 0 Then

            EncodeMessageRichTextBox.Text += $"{vbCrLf}В последовательности не обнаружено ошибок!" + $" ({encoded.ToBinString().Length} бит)"

            restored = decod.Decode(encoded)

        Else

            Dim errEncoded = GetMessageWithErrors(encoded, PercentOfErrorF)

            EncodeMessageRichTextBox.Text += $"{vbCrLf}В последовательности допущены ошибки!" + $" ({encoded.ToBinString().Length} бит)"

            EncodeMessageRichTextBox.Text += vbCrLf + errEncoded.ToBinString

            restored = decod.Decode(errEncoded)

        End If

        DecodeMessageRichTextBox.Text = restored.ToBinString() + $" ({restored.ToBinString().Length} бит)"

        If BoolKeyF = True Then

            UtfMessageRichTextBox.Text += $"Декодированное сообщение: {restored.ToBinString()}{vbCrLf}"

        Else

            UtfMessageRichTextBox.Text += $"Декодированное сообщение: {restored.ToUtf8String()}{vbCrLf}"

        End If

        UtfMessageRichTextBox.Text += $"Исходное сообщение: {MessageF}{vbCrLf}"

    End Sub

    Private Sub EncodeDecodeForm_Load(sender As Object, e As EventArgs) Handles MyBase.Load

    End Sub
End Class