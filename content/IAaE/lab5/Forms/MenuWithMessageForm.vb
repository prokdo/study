Imports System.Text.RegularExpressions
Imports System.Windows.Forms.VisualStyles.VisualStyleElement.Button

Public Class MenuWithMessageForm

    Private KeyF As Boolean
    Private MessageF As String

    Private ChangeGeneratingPolynomialsFormInstance As ChangeGeneratingPolynomialsForm
    Private ShowRegisterStatesFormInstance As ShowRegisterStatesForm
    Private EncodeDecodeFormInstance As EncodeDecodeForm

    Dim coder As New Viterbi.Encoder({5, 7})

    Public Shared ResultValueVariable As String

    Public Shared PolynomialValueList As New List(Of Integer)()

    Dim BoolKey As Boolean = False

    Public Sub New(Key As Boolean, Message As String)

        InitializeComponent()

        KeyF = Key
        MessageF = Message

        ProbabilityOfErrorComboBox.SelectedIndex = 0

        GeneratingPolynomialValueLabel.Text = coder.ToString()
        RegisterLengthValueLabel.Text = coder.RegisterLength

        PolynomialValueList.Clear()
        PolynomialValueList.Add(coder.ToString())

        MessageTextBox.Text = MessageF

        If MessageTextBox.Text.Length > 0 Then

            EncodeDecodeButton.Enabled = True

        End If

    End Sub

    Private Sub PolynomialTextBox_KeyPress(sender As Object, e As KeyPressEventArgs)

        If (Not Char.IsDigit(e.KeyChar)) AndAlso (e.KeyChar <> "-"c) AndAlso (e.KeyChar <> ControlChars.Back) Then

            e.Handled = True

        End If

    End Sub

    Private Sub ChangeValueButton_Click(sender As Object, e As EventArgs) Handles ChangeValueButton.Click

        ChangeGeneratingPolynomialsFormInstance = New ChangeGeneratingPolynomialsForm(Me)

        ChangeGeneratingPolynomialsFormInstance.ShowDialog()

        CheckCoderValue(ResultValueVariable)

        Dim IntArray() As Integer = PolynomialValueList.ToArray

        Try

            Dim coder As New Viterbi.Encoder(IntArray)

            GeneratingPolynomialValueLabel.Text = coder.ToString()

            RegisterLengthValueLabel.Text = coder.RegisterLength

        Catch Exception As Exception

            Dim MessageInfo As String = "Инициализация порождающих многочленов с такими значениями невозможна." & vbCrLf & vbCrLf & "Установлены данные по умолчанию"

            GeneratingPolynomialValueLabel.Text = "(5,7)"

            RegisterLengthValueLabel.Text = "3"

            MessageBox.Show(MessageInfo, "Произошло исключение", MessageBoxButtons.OK, MessageBoxIcon.Error)

        End Try

    End Sub

    Private Sub ShowRegisterStatesButton_Click(sender As Object, e As EventArgs) Handles ShowRegisterStatesButton.Click

        CheckCoderValue(GeneratingPolynomialValueLabel.Text)

        Dim IntArray() As Integer = PolynomialValueList.ToArray

        ShowRegisterStatesFormInstance = New ShowRegisterStatesForm(IntArray)

        ShowRegisterStatesFormInstance.ShowDialog()

    End Sub

    Private Sub MessageTextBox_TextChanged(sender As Object, e As EventArgs) Handles MessageTextBox.TextChanged

        If (MessageTextBox.Text.Length > 1) Then

            EncodeDecodeButton.Enabled = True

        Else

            EncodeDecodeButton.Enabled = False

        End If

    End Sub

    Private Sub CyclicErrorCheckBox_CheckedChanged(sender As Object, e As EventArgs) Handles CyclicErrorCheckBox.CheckedChanged

        If CyclicErrorCheckBox.Checked Then

            ProbabilityOfErrorComboBox.Enabled = False

        Else

            ProbabilityOfErrorComboBox.Enabled = True

        End If

    End Sub

    Private Sub BitSequenceCheckBox_CheckedChanged(sender As Object, e As EventArgs) Handles BitSequenceCheckBox.CheckedChanged

        If BitSequenceCheckBox.Checked Then

            Dim ContainsOnlyBool As Boolean = MessageTextBox.Text.All(Function(c) c = "0"c OrElse c = "1"c)

            If ContainsOnlyBool Then

                MessageTextBox.ReadOnly = True

                BoolKey = True

            Else

                MessageBox.Show($"Строка содержит другие символы кроме '0' и '1' {vbCrLf}{vbCrLf}Условие сброшено", "", MessageBoxButtons.OK, MessageBoxIcon.Exclamation)

                BitSequenceCheckBox.Checked = False

            End If

        Else


        End If

    End Sub

    Private Sub EncodeDecodeButton_Click(sender As Object, e As EventArgs) Handles EncodeDecodeButton.Click

        CheckCoderValue(GeneratingPolynomialValueLabel.Text)

        Dim PolynomialValueArr() As Integer = PolynomialValueList.ToArray


        If CyclicErrorCheckBox.Checked Then

            EncodeDecodeFormInstance = New EncodeDecodeForm(PolynomialValueArr, MessageTextBox.Text, "-1", BoolKey)

        Else

            Dim PercentString As String = ProbabilityOfErrorComboBox.SelectedItem.ToString()

            PercentString = PercentString.Remove(PercentString.Length - 1)

            EncodeDecodeFormInstance = New EncodeDecodeForm(PolynomialValueArr, MessageTextBox.Text, Integer.Parse(PercentString), BoolKey)

        End If

        EncodeDecodeFormInstance.ShowDialog()

    End Sub

    Private Sub CheckCoderValue(StringValue As String)

        Dim StringArray() As String = StringValue.Split(","c)

        Dim IntArray(StringArray.Length - 1) As Integer

        For i As Integer = 0 To StringArray.Length - 1

            Dim NumericOnly As String = Regex.Replace(StringArray(i), "[^\d]", "")

            Dim IntValue As Integer

            If Integer.TryParse(NumericOnly, IntValue) Then

                IntArray(i) = IntValue

            Else

                Dim MessageInfo As String = "Инициализация порождающих многочленов с такими значениями невозможна." & vbCrLf & vbCrLf & "Установлены данные по умолчанию"

                MessageBox.Show(MessageInfo, "Произошло исключение", MessageBoxButtons.OK, MessageBoxIcon.Information)

                GeneratingPolynomialValueLabel.Text = "(5,7)"

                RegisterLengthValueLabel.Text = "3"

                Return

            End If

        Next

        PolynomialValueList.Clear()

        PolynomialValueList.AddRange(IntArray)

    End Sub

    Private Sub MenuWithMessageForm_Load(sender As Object, e As EventArgs) Handles MyBase.Load

    End Sub
End Class