<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()>
Partial Class MainForm
    Inherits System.Windows.Forms.Form

    'Форма переопределяет dispose для очистки списка компонентов.
    <System.Diagnostics.DebuggerNonUserCode()>
    Protected Overrides Sub Dispose(ByVal disposing As Boolean)
        Try
            If disposing AndAlso components IsNot Nothing Then
                components.Dispose()
            End If
        Finally
            MyBase.Dispose(disposing)
        End Try
    End Sub

    'Является обязательной для конструктора форм Windows Forms
    Private components As System.ComponentModel.IContainer

    'Примечание: следующая процедура является обязательной для конструктора форм Windows Forms
    'Для ее изменения используйте конструктор форм Windows Form.  
    'Не изменяйте ее в редакторе исходного кода.
    <System.Diagnostics.DebuggerStepThrough()>
    Private Sub InitializeComponent()
        Me.HandSolveButton = New System.Windows.Forms.Button()
        Me.FileSolveButton = New System.Windows.Forms.Button()
        Me.Label2 = New System.Windows.Forms.Label()
        Me.EncodeTypeComboBox = New System.Windows.Forms.ComboBox()
        Me.SuspendLayout()
        '
        'HandSolveButton
        '
        Me.HandSolveButton.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.HandSolveButton.Location = New System.Drawing.Point(17, 75)
        Me.HandSolveButton.Name = "HandSolveButton"
        Me.HandSolveButton.Size = New System.Drawing.Size(237, 83)
        Me.HandSolveButton.TabIndex = 0
        Me.HandSolveButton.Text = "Ручной ввод"
        Me.HandSolveButton.UseVisualStyleBackColor = True
        '
        'FileSolveButton
        '
        Me.FileSolveButton.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.FileSolveButton.Location = New System.Drawing.Point(292, 75)
        Me.FileSolveButton.Name = "FileSolveButton"
        Me.FileSolveButton.Size = New System.Drawing.Size(237, 83)
        Me.FileSolveButton.TabIndex = 1
        Me.FileSolveButton.Text = "Чтение из файла"
        Me.FileSolveButton.UseVisualStyleBackColor = True
        '
        'Label2
        '
        Me.Label2.AutoSize = True
        Me.Label2.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.Label2.Location = New System.Drawing.Point(17, 22)
        Me.Label2.Name = "Label2"
        Me.Label2.Size = New System.Drawing.Size(376, 29)
        Me.Label2.TabIndex = 2
        Me.Label2.Text = "Выберите желаемое действие:"
        '
        'EncodeTypeComboBox
        '
        Me.EncodeTypeComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList
        Me.EncodeTypeComboBox.Font = New System.Drawing.Font("Microsoft Sans Serif", 15.75!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.EncodeTypeComboBox.FormattingEnabled = True
        Me.EncodeTypeComboBox.Items.AddRange(New Object() {"Windows-1251", "UTF-8", "ASCII"})
        Me.EncodeTypeComboBox.Location = New System.Drawing.Point(389, 22)
        Me.EncodeTypeComboBox.Name = "EncodeTypeComboBox"
        Me.EncodeTypeComboBox.Size = New System.Drawing.Size(140, 33)
        Me.EncodeTypeComboBox.TabIndex = 3
        '
        'MainForm
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(546, 170)
        Me.Controls.Add(Me.EncodeTypeComboBox)
        Me.Controls.Add(Me.Label2)
        Me.Controls.Add(Me.FileSolveButton)
        Me.Controls.Add(Me.HandSolveButton)
        Me.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle
        Me.Margin = New System.Windows.Forms.Padding(2, 3, 2, 3)
        Me.MaximizeBox = False
        Me.Name = "MainForm"
        Me.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen
        Me.Text = "ЛР-5-АИКИ"
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub

    Friend WithEvents HandSolveButton As Button
    Friend WithEvents FileSolveButton As Button
    Friend WithEvents Label2 As Label
    Friend WithEvents EncodeTypeComboBox As ComboBox
End Class
