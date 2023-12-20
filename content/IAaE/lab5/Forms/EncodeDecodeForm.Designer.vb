<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()>
Partial Class EncodeDecodeForm
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
        Me.EncodeDecodeLabel = New System.Windows.Forms.Label()
        Me.EncodeMessageLabel = New System.Windows.Forms.Label()
        Me.BitMessageRichTextBox = New System.Windows.Forms.RichTextBox()
        Me.BitMessageLabel = New System.Windows.Forms.Label()
        Me.EncodeMessageRichTextBox = New System.Windows.Forms.RichTextBox()
        Me.DecodeMessageLabel = New System.Windows.Forms.Label()
        Me.UtfMessageRichTextBox = New System.Windows.Forms.RichTextBox()
        Me.UtfMessageLabel = New System.Windows.Forms.Label()
        Me.DecodeMessageRichTextBox = New System.Windows.Forms.RichTextBox()
        Me.SuspendLayout()
        '
        'EncodeDecodeLabel
        '
        Me.EncodeDecodeLabel.AutoSize = True
        Me.EncodeDecodeLabel.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Underline, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.EncodeDecodeLabel.Location = New System.Drawing.Point(177, 11)
        Me.EncodeDecodeLabel.Name = "EncodeDecodeLabel"
        Me.EncodeDecodeLabel.Size = New System.Drawing.Size(380, 29)
        Me.EncodeDecodeLabel.TabIndex = 0
        Me.EncodeDecodeLabel.Text = "Кодирование и Декодирование"
        '
        'EncodeMessageLabel
        '
        Me.EncodeMessageLabel.AutoSize = True
        Me.EncodeMessageLabel.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.EncodeMessageLabel.Location = New System.Drawing.Point(112, 170)
        Me.EncodeMessageLabel.Name = "EncodeMessageLabel"
        Me.EncodeMessageLabel.Size = New System.Drawing.Size(588, 29)
        Me.EncodeMessageLabel.TabIndex = 1
        Me.EncodeMessageLabel.Text = "Стадия кодирования и повреждения сообщения:"
        '
        'BitMessageRichTextBox
        '
        Me.BitMessageRichTextBox.Font = New System.Drawing.Font("Microsoft Sans Serif", 15.75!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.BitMessageRichTextBox.Location = New System.Drawing.Point(18, 97)
        Me.BitMessageRichTextBox.Name = "BitMessageRichTextBox"
        Me.BitMessageRichTextBox.ReadOnly = True
        Me.BitMessageRichTextBox.Size = New System.Drawing.Size(631, 61)
        Me.BitMessageRichTextBox.TabIndex = 2
        Me.BitMessageRichTextBox.TabStop = False
        Me.BitMessageRichTextBox.Text = ""
        '
        'BitMessageLabel
        '
        Me.BitMessageLabel.AutoSize = True
        Me.BitMessageLabel.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.BitMessageLabel.Location = New System.Drawing.Point(53, 58)
        Me.BitMessageLabel.Name = "BitMessageLabel"
        Me.BitMessageLabel.Size = New System.Drawing.Size(678, 29)
        Me.BitMessageLabel.TabIndex = 3
        Me.BitMessageLabel.Text = "Cообщение до кодирования в двоичном представлении:"
        '
        'EncodeMessageRichTextBox
        '
        Me.EncodeMessageRichTextBox.Font = New System.Drawing.Font("Microsoft Sans Serif", 15.75!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.EncodeMessageRichTextBox.Location = New System.Drawing.Point(18, 209)
        Me.EncodeMessageRichTextBox.Name = "EncodeMessageRichTextBox"
        Me.EncodeMessageRichTextBox.ReadOnly = True
        Me.EncodeMessageRichTextBox.Size = New System.Drawing.Size(631, 111)
        Me.EncodeMessageRichTextBox.TabIndex = 4
        Me.EncodeMessageRichTextBox.TabStop = False
        Me.EncodeMessageRichTextBox.Text = ""
        '
        'DecodeMessageLabel
        '
        Me.DecodeMessageLabel.AutoSize = True
        Me.DecodeMessageLabel.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.DecodeMessageLabel.Location = New System.Drawing.Point(51, 332)
        Me.DecodeMessageLabel.Name = "DecodeMessageLabel"
        Me.DecodeMessageLabel.Size = New System.Drawing.Size(683, 29)
        Me.DecodeMessageLabel.TabIndex = 5
        Me.DecodeMessageLabel.Text = "Декодированное сообщение в двоичном представлении:"
        '
        'UtfMessageRichTextBox
        '
        Me.UtfMessageRichTextBox.Font = New System.Drawing.Font("Microsoft Sans Serif", 15.75!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.UtfMessageRichTextBox.Location = New System.Drawing.Point(18, 533)
        Me.UtfMessageRichTextBox.Name = "UtfMessageRichTextBox"
        Me.UtfMessageRichTextBox.ReadOnly = True
        Me.UtfMessageRichTextBox.Size = New System.Drawing.Size(631, 111)
        Me.UtfMessageRichTextBox.TabIndex = 6
        Me.UtfMessageRichTextBox.TabStop = False
        Me.UtfMessageRichTextBox.Text = ""
        '
        'UtfMessageLabel
        '
        Me.UtfMessageLabel.AutoSize = True
        Me.UtfMessageLabel.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.UtfMessageLabel.Location = New System.Drawing.Point(203, 494)
        Me.UtfMessageLabel.Name = "UtfMessageLabel"
        Me.UtfMessageLabel.Size = New System.Drawing.Size(306, 29)
        Me.UtfMessageLabel.TabIndex = 7
        Me.UtfMessageLabel.Text = "Результаты вычислений:"
        '
        'DecodeMessageRichTextBox
        '
        Me.DecodeMessageRichTextBox.Font = New System.Drawing.Font("Microsoft Sans Serif", 15.75!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.DecodeMessageRichTextBox.Location = New System.Drawing.Point(18, 371)
        Me.DecodeMessageRichTextBox.Name = "DecodeMessageRichTextBox"
        Me.DecodeMessageRichTextBox.ReadOnly = True
        Me.DecodeMessageRichTextBox.Size = New System.Drawing.Size(631, 111)
        Me.DecodeMessageRichTextBox.TabIndex = 8
        Me.DecodeMessageRichTextBox.TabStop = False
        Me.DecodeMessageRichTextBox.Text = ""
        '
        'EncodeDecodeForm
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(667, 661)
        Me.Controls.Add(Me.DecodeMessageRichTextBox)
        Me.Controls.Add(Me.UtfMessageLabel)
        Me.Controls.Add(Me.UtfMessageRichTextBox)
        Me.Controls.Add(Me.DecodeMessageLabel)
        Me.Controls.Add(Me.EncodeMessageRichTextBox)
        Me.Controls.Add(Me.BitMessageLabel)
        Me.Controls.Add(Me.BitMessageRichTextBox)
        Me.Controls.Add(Me.EncodeMessageLabel)
        Me.Controls.Add(Me.EncodeDecodeLabel)
        Me.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle
        Me.Margin = New System.Windows.Forms.Padding(2, 3, 2, 3)
        Me.MaximizeBox = False
        Me.Name = "EncodeDecodeForm"
        Me.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen
        Me.Text = "ЛР-5-АИКИ"
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub

    Friend WithEvents EncodeDecodeLabel As Label
    Friend WithEvents EncodeMessageLabel As Label
    Friend WithEvents BitMessageRichTextBox As RichTextBox
    Friend WithEvents BitMessageLabel As Label
    Friend WithEvents EncodeMessageRichTextBox As RichTextBox
    Friend WithEvents DecodeMessageLabel As Label
    Friend WithEvents UtfMessageRichTextBox As RichTextBox
    Friend WithEvents UtfMessageLabel As Label
    Friend WithEvents DecodeMessageRichTextBox As RichTextBox
End Class
