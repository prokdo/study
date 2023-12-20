Imports System.IO
Imports System.Text

Public Class MainForm

    Private MenuWithMessageFormInstance As MenuWithMessageForm

    Public Sub New()

        InitializeComponent()

        EncodeTypeComboBox.SelectedIndex = 0

    End Sub


    Private Sub HandSolveButton_Click(sender As Object, e As EventArgs) Handles HandSolveButton.Click

        MenuWithMessageFormInstance = New MenuWithMessageForm(False, "")

        MenuWithMessageFormInstance.ShowDialog()

    End Sub

    Private Sub FileSolveButton_Click(sender As Object, e As EventArgs) Handles FileSolveButton.Click

        Dim OpenFileDialog As New OpenFileDialog With
        {
            .Title = "Выберите файл",
            .Filter = "Все файлы (*.*)|*.*"
        }

        Dim Message As String = ""

        If OpenFileDialog.ShowDialog() = DialogResult.OK Then

            Dim FilePath As String = OpenFileDialog.FileName

            Try

                Dim EncodeType As String = EncodeTypeComboBox.SelectedItem.ToString

                Message = File.ReadAllText(FilePath, Encoding.GetEncoding(EncodeType))

            Catch Exception As Exception

                MessageBox.Show("Ошибка при чтении файла: " & Exception.Message)

                Return

            End Try

        End If

        MenuWithMessageFormInstance = New MenuWithMessageForm(True, Message)

        MenuWithMessageFormInstance.ShowDialog()

    End Sub

    Private Sub Label2_Click(sender As Object, e As EventArgs) Handles Label2.Click

    End Sub

    Private Sub MainForm_Load(sender As Object, e As EventArgs) Handles MyBase.Load

    End Sub
End Class