Public Class ChangeGeneratingPolynomialsForm

    Private MenuWithMessageFormInstance As MenuWithMessageForm

    Dim ChangedValueResult As String

    Public Sub New(MenuWithMessageFormInstance As MenuWithMessageForm)

        InitializeComponent()

        Me.MenuWithMessageFormInstance = MenuWithMessageFormInstance

        If ChangedValueResult = "" Then

            ChangedValueResult = "(5,7)"

            ValueToSet(ChangedValueResult)

        End If


    End Sub

    Private Sub ChangePolynomialsTextBox_KeyPress(sender As Object, e As KeyPressEventArgs) Handles ChangePolynomialsTextBox.KeyPress

        If Not Char.IsDigit(e.KeyChar) AndAlso e.KeyChar <> "," AndAlso e.KeyChar <> ChrW(Keys.Back) Then

            e.Handled = True

        End If

    End Sub

    Private Sub ApplyValueButton_Click(sender As Object, e As EventArgs) Handles ApplyValueButton.Click

        Dim ChangedValue As String = ChangePolynomialsTextBox.Text.ToString()

        Dim ChangedValueTrimmed As String = ChangedValue.Trim(","c)

        ChangedValueResult = $"({ChangedValueTrimmed})"

        ValueToSet(ChangedValueResult)

        Me.Close()

    End Sub

    Sub ValueToSet(ChangedValueResult As String)

        MenuWithMessageForm.ResultValueVariable = ChangedValueResult

    End Sub

    Private Sub ChangeGeneratingPolynomialsForm_Load(sender As Object, e As EventArgs) Handles MyBase.Load

    End Sub
End Class