Public Class ShowRegisterStatesForm

    Dim IntArrayF() As Integer

    Dim myDataTable As New DataTable()

    Public Sub New(IntArray() As Integer)

        InitializeComponent()

        IntArrayF = DirectCast(IntArray.Clone(), Integer())

        InsertData()

    End Sub

    Private Sub InsertData()

        Controls.Add(DataGridView1)

        DataGridView1.DataSource = myDataTable

        DataGridView1.Font = New Font("GOST Type B", 16)

        DataGridView1.AutoGenerateColumns = True
        DataGridView1.AllowUserToAddRows = False
        DataGridView1.DefaultCellStyle.Alignment = DataGridViewContentAlignment.MiddleCenter
        DataGridView1.ColumnHeadersDefaultCellStyle.Alignment = DataGridViewContentAlignment.MiddleCenter

        Dim coder As New Viterbi.Encoder(IntArrayF)

        myDataTable.Columns.Add("№", GetType(String))
        myDataTable.Columns.Add("Состояние регистра", GetType(String))
        myDataTable.Columns.Add("Комбинация на выходе", GetType(String))

        For Each column As DataGridViewColumn In DataGridView1.Columns
            column.SortMode = DataGridViewColumnSortMode.NotSortable
        Next

        For Each state In coder.CoderStates

            myDataTable.Rows.Add(state.Key,
                                 Convert.ToString(state.Key, 2).PadLeft(coder.RegisterLength, "0"c),
                                    Convert.ToString(state.Value, 2).PadLeft(coder.Polynoms.Length, "0"c))

        Next

    End Sub

    Private Sub ShowRegisterStatesForm_Load(sender As Object, e As EventArgs) Handles MyBase.Load

    End Sub
End Class