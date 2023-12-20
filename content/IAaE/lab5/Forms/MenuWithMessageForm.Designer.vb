<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()>
Partial Class MenuWithMessageForm
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
        Me.MessageLabel = New System.Windows.Forms.Label()
        Me.MessageTextBox = New System.Windows.Forms.RichTextBox()
        Me.GeneratingPolynomialLabel = New System.Windows.Forms.Label()
        Me.GeneratingPolynomialValueLabel = New System.Windows.Forms.Label()
        Me.ChangeValueButton = New System.Windows.Forms.Button()
        Me.RegisterLengthLabel = New System.Windows.Forms.Label()
        Me.ShowRegisterStatesButton = New System.Windows.Forms.Button()
        Me.RegisterLengthValueLabel = New System.Windows.Forms.Label()
        Me.EncodeDecodeButton = New System.Windows.Forms.Button()
        Me.Label2 = New System.Windows.Forms.Label()
        Me.ProbabilityOfErrorComboBox = New System.Windows.Forms.ComboBox()
        Me.CyclicErrorCheckBox = New System.Windows.Forms.CheckBox()
        Me.BitSequenceCheckBox = New System.Windows.Forms.CheckBox()
        Me.SuspendLayout()
        '
        'Label1
        '
        Me.Label1.AutoSize = True
        Me.Label1.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Underline, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.Label1.Location = New System.Drawing.Point(203, 18)
        Me.Label1.Name = "Label1"
        Me.Label1.Size = New System.Drawing.Size(351, 29)
        Me.Label1.TabIndex = 0
        Me.Label1.Text = "Свёрточный код по Витерби:"
        '
        'MessageLabel
        '
        Me.MessageLabel.AutoSize = True
        Me.MessageLabel.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.MessageLabel.Location = New System.Drawing.Point(181, 179)
        Me.MessageLabel.Name = "MessageLabel"
        Me.MessageLabel.Size = New System.Drawing.Size(412, 29)
        Me.MessageLabel.TabIndex = 1
        Me.MessageLabel.Text = "Кодируемая последовательность:"
        '
        'MessageTextBox
        '
        Me.MessageTextBox.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.MessageTextBox.Location = New System.Drawing.Point(12, 216)
        Me.MessageTextBox.Name = "MessageTextBox"
        Me.MessageTextBox.Size = New System.Drawing.Size(683, 192)
        Me.MessageTextBox.TabIndex = 2
        Me.MessageTextBox.TabStop = False
        Me.MessageTextBox.Text = ""
        '
        'GeneratingPolynomialLabel
        '
        Me.GeneratingPolynomialLabel.AutoSize = True
        Me.GeneratingPolynomialLabel.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.GeneratingPolynomialLabel.Location = New System.Drawing.Point(12, 70)
        Me.GeneratingPolynomialLabel.Name = "GeneratingPolynomialLabel"
        Me.GeneratingPolynomialLabel.Size = New System.Drawing.Size(255, 29)
        Me.GeneratingPolynomialLabel.TabIndex = 3
        Me.GeneratingPolynomialLabel.Text = "Порож. многочлены:"
        '
        'GeneratingPolynomialValueLabel
        '
        Me.GeneratingPolynomialValueLabel.AutoSize = True
        Me.GeneratingPolynomialValueLabel.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.GeneratingPolynomialValueLabel.Location = New System.Drawing.Point(213, 70)
        Me.GeneratingPolynomialValueLabel.Name = "GeneratingPolynomialValueLabel"
        Me.GeneratingPolynomialValueLabel.Size = New System.Drawing.Size(0, 29)
        Me.GeneratingPolynomialValueLabel.TabIndex = 4
        '
        'ChangeValueButton
        '
        Me.ChangeValueButton.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.ChangeValueButton.Location = New System.Drawing.Point(12, 115)
        Me.ChangeValueButton.Name = "ChangeValueButton"
        Me.ChangeValueButton.Size = New System.Drawing.Size(329, 46)
        Me.ChangeValueButton.TabIndex = 5
        Me.ChangeValueButton.TabStop = False
        Me.ChangeValueButton.Text = "Изменить значения"
        Me.ChangeValueButton.UseVisualStyleBackColor = True
        '
        'RegisterLengthLabel
        '
        Me.RegisterLengthLabel.AutoSize = True
        Me.RegisterLengthLabel.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.RegisterLengthLabel.Location = New System.Drawing.Point(361, 70)
        Me.RegisterLengthLabel.Name = "RegisterLengthLabel"
        Me.RegisterLengthLabel.Size = New System.Drawing.Size(288, 29)
        Me.RegisterLengthLabel.TabIndex = 6
        Me.RegisterLengthLabel.Text = "Длина регистра сдвига:"
        '
        'ShowRegisterStatesButton
        '
        Me.ShowRegisterStatesButton.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.ShowRegisterStatesButton.Location = New System.Drawing.Point(366, 115)
        Me.ShowRegisterStatesButton.Name = "ShowRegisterStatesButton"
        Me.ShowRegisterStatesButton.Size = New System.Drawing.Size(329, 46)
        Me.ShowRegisterStatesButton.TabIndex = 7
        Me.ShowRegisterStatesButton.TabStop = False
        Me.ShowRegisterStatesButton.Text = "Регистр состояний"
        Me.ShowRegisterStatesButton.UseVisualStyleBackColor = True
        '
        'RegisterLengthValueLabel
        '
        Me.RegisterLengthValueLabel.AutoSize = True
        Me.RegisterLengthValueLabel.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.RegisterLengthValueLabel.Location = New System.Drawing.Point(613, 70)
        Me.RegisterLengthValueLabel.Name = "RegisterLengthValueLabel"
        Me.RegisterLengthValueLabel.Size = New System.Drawing.Size(0, 29)
        Me.RegisterLengthValueLabel.TabIndex = 8
        '
        'EncodeDecodeButton
        '
        Me.EncodeDecodeButton.Enabled = False
        Me.EncodeDecodeButton.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.EncodeDecodeButton.Location = New System.Drawing.Point(12, 600)
        Me.EncodeDecodeButton.Name = "EncodeDecodeButton"
        Me.EncodeDecodeButton.Size = New System.Drawing.Size(683, 46)
        Me.EncodeDecodeButton.TabIndex = 9
        Me.EncodeDecodeButton.TabStop = False
        Me.EncodeDecodeButton.Text = "Перейти к кодированию и декодированию"
        Me.EncodeDecodeButton.UseVisualStyleBackColor = True
        '
        'Label2
        '
        Me.Label2.AutoSize = True
        Me.Label2.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.Label2.Location = New System.Drawing.Point(169, 466)
        Me.Label2.Name = "Label2"
        Me.Label2.Size = New System.Drawing.Size(432, 29)
        Me.Label2.TabIndex = 10
        Me.Label2.Text = "Вероятность ошибок при передаче:"
        '
        'ProbabilityOfErrorComboBox
        '
        Me.ProbabilityOfErrorComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList
        Me.ProbabilityOfErrorComboBox.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.ProbabilityOfErrorComboBox.FormattingEnabled = True
        Me.ProbabilityOfErrorComboBox.Items.AddRange(New Object() {"0%", "5%", "10%", "15%", "25%", "50%", "75%", "100%"})
        Me.ProbabilityOfErrorComboBox.Location = New System.Drawing.Point(218, 506)
        Me.ProbabilityOfErrorComboBox.Name = "ProbabilityOfErrorComboBox"
        Me.ProbabilityOfErrorComboBox.Size = New System.Drawing.Size(286, 37)
        Me.ProbabilityOfErrorComboBox.TabIndex = 11
        Me.ProbabilityOfErrorComboBox.TabStop = False
        '
        'CyclicErrorCheckBox
        '
        Me.CyclicErrorCheckBox.AutoSize = True
        Me.CyclicErrorCheckBox.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.CyclicErrorCheckBox.Location = New System.Drawing.Point(183, 554)
        Me.CyclicErrorCheckBox.Name = "CyclicErrorCheckBox"
        Me.CyclicErrorCheckBox.Size = New System.Drawing.Size(433, 33)
        Me.CyclicErrorCheckBox.TabIndex = 12
        Me.CyclicErrorCheckBox.TabStop = False
        Me.CyclicErrorCheckBox.Text = "Циклическая вероятность ошибок"
        Me.CyclicErrorCheckBox.UseVisualStyleBackColor = True
        '
        'BitSequenceCheckBox
        '
        Me.BitSequenceCheckBox.AutoSize = True
        Me.BitSequenceCheckBox.Font = New System.Drawing.Font("Microsoft Sans Serif", 18.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.BitSequenceCheckBox.Location = New System.Drawing.Point(101, 417)
        Me.BitSequenceCheckBox.Name = "BitSequenceCheckBox"
        Me.BitSequenceCheckBox.Size = New System.Drawing.Size(569, 33)
        Me.BitSequenceCheckBox.TabIndex = 13
        Me.BitSequenceCheckBox.Text = "Побитовая последовательность (только 0 и 1)"
        Me.BitSequenceCheckBox.UseVisualStyleBackColor = True
        '
        'MenuWithMessageForm
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(707, 656)
        Me.Controls.Add(Me.BitSequenceCheckBox)
        Me.Controls.Add(Me.CyclicErrorCheckBox)
        Me.Controls.Add(Me.ProbabilityOfErrorComboBox)
        Me.Controls.Add(Me.Label2)
        Me.Controls.Add(Me.EncodeDecodeButton)
        Me.Controls.Add(Me.RegisterLengthValueLabel)
        Me.Controls.Add(Me.ShowRegisterStatesButton)
        Me.Controls.Add(Me.RegisterLengthLabel)
        Me.Controls.Add(Me.ChangeValueButton)
        Me.Controls.Add(Me.GeneratingPolynomialValueLabel)
        Me.Controls.Add(Me.GeneratingPolynomialLabel)
        Me.Controls.Add(Me.MessageTextBox)
        Me.Controls.Add(Me.MessageLabel)
        Me.Controls.Add(Me.Label1)
        Me.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(204, Byte))
        Me.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle
        Me.Margin = New System.Windows.Forms.Padding(2, 3, 2, 3)
        Me.MaximizeBox = False
        Me.Name = "MenuWithMessageForm"
        Me.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen
        Me.Text = "ЛР-5-АИКИ"
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub

    Friend WithEvents Label1 As Label
    Friend WithEvents MessageLabel As Label
    Friend WithEvents MessageTextBox As RichTextBox
    Friend WithEvents GeneratingPolynomialLabel As Label
    Friend WithEvents GeneratingPolynomialValueLabel As Label
    Friend WithEvents ChangeValueButton As Button
    Friend WithEvents RegisterLengthLabel As Label
    Friend WithEvents ShowRegisterStatesButton As Button
    Friend WithEvents RegisterLengthValueLabel As Label
    Friend WithEvents EncodeDecodeButton As Button
    Friend WithEvents Label2 As Label
    Friend WithEvents ProbabilityOfErrorComboBox As ComboBox
    Friend WithEvents CyclicErrorCheckBox As CheckBox
    Friend WithEvents BitSequenceCheckBox As CheckBox
End Class
