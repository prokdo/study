<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()>
Partial Class ChangeGeneratingPolynomialsForm
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
        Me.Label1 = New System.Windows.Forms.Label()
        Me.ChangePolynomialsTextBox = New System.Windows.Forms.TextBox()
        Me.ApplyValueButton = New System.Windows.Forms.Button()
        Me.Label2 = New System.Windows.Forms.Label()
        Me.SuspendLayout()
        '
        'Label1
        '
        Me.Label1.AutoSize = True
        Me.Label1.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Underline, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.Label1.Location = New System.Drawing.Point(7, 19)
        Me.Label1.Name = "Label1"
        Me.Label1.Size = New System.Drawing.Size(490, 29)
        Me.Label1.TabIndex = 0
        Me.Label1.Text = "Изменение порождающих многочленов:"
        '
        'ChangePolynomialsTextBox
        '
        Me.ChangePolynomialsTextBox.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.ChangePolynomialsTextBox.Location = New System.Drawing.Point(12, 101)
        Me.ChangePolynomialsTextBox.MaxLength = 1555555
        Me.ChangePolynomialsTextBox.Name = "ChangePolynomialsTextBox"
        Me.ChangePolynomialsTextBox.Size = New System.Drawing.Size(390, 35)
        Me.ChangePolynomialsTextBox.TabIndex = 1
        Me.ChangePolynomialsTextBox.TabStop = False
        '
        'ApplyValueButton
        '
        Me.ApplyValueButton.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.ApplyValueButton.Location = New System.Drawing.Point(12, 155)
        Me.ApplyValueButton.Name = "ApplyValueButton"
        Me.ApplyValueButton.Size = New System.Drawing.Size(390, 42)
        Me.ApplyValueButton.TabIndex = 2
        Me.ApplyValueButton.Text = "Применить"
        Me.ApplyValueButton.UseVisualStyleBackColor = True
        '
        'Label2
        '
        Me.Label2.AutoSize = True
        Me.Label2.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.Label2.Location = New System.Drawing.Point(34, 59)
        Me.Label2.Name = "Label2"
        Me.Label2.Size = New System.Drawing.Size(395, 29)
        Me.Label2.TabIndex = 3
        Me.Label2.Text = "(В любом кол-ве, через запятую)"
        '
        'ChangeGeneratingPolynomialsForm
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(415, 208)
        Me.Controls.Add(Me.Label2)
        Me.Controls.Add(Me.ApplyValueButton)
        Me.Controls.Add(Me.ChangePolynomialsTextBox)
        Me.Controls.Add(Me.Label1)
        Me.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle
        Me.Margin = New System.Windows.Forms.Padding(2, 3, 2, 3)
        Me.MaximizeBox = False
        Me.Name = "ChangeGeneratingPolynomialsForm"
        Me.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen
        Me.Text = "ЛР-5-АИКИ"
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub

    Friend WithEvents Label1 As Label
    Friend WithEvents ChangePolynomialsTextBox As TextBox
    Friend WithEvents ApplyValueButton As Button
    Friend WithEvents Label2 As Label
End Class
