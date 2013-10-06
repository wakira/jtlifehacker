package cn.edu.sjtu.se.jtlifehacker;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

public class EncryptedEditTextPreference extends EditTextPreference {

	public EncryptedEditTextPreference(Context context) {
		super(context);
	}
	public EncryptedEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public EncryptedEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
	@Override
	public String getText() {
		// Decrypt 
		String encrypted = super.getText();
		String decrypted = encrypted; // TODO replace it with working code
		return decrypted;
	}
	@Override
	public void setText(String text) {
		// TODO Encrypt text
		super.setText(text);
	}
    
}
