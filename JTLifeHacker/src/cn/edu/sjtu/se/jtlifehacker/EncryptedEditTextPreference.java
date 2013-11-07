package cn.edu.sjtu.se.jtlifehacker;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;


class Encryptor
{
	private final char answerOfTheWorld=1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1;
	private final char answerOfMyWord=(answerOfTheWorld>>1)/(1+1+1+1+1+1+1+1+1+1+1+1-1-1-1-1-1-1-1-1-1);
	private char hash(char in)
	{
		int var1=in;
		int var2=answerOfMyWord;
		int var3=(var1+var2)%255;
		return (char) var3;
	}
	private char dehash(char in)
	{
		int var1=in;
		int var2=answerOfMyWord;
		int var3=(var1-var2+255)%255;
		return (char) var3;
	}
	public String encrypt(String text)
	{
		if (text==null || text.length()<=2) return text;
		String tmp1,tmp2;
		tmp2=text;
		for (int i=0;i<answerOfTheWorld;i++)
		{
			tmp1="";
			if (i%2==1) tmp1+=tmp2.charAt(0);
			for (int j=0;j<(text.length()+1)/2-1;j++)
			{
				tmp1=tmp1+hash(tmp2.charAt(j*2+1+i%2));
				tmp1=tmp1+hash(tmp2.charAt(j*2+i%2));
			}
			tmp2=tmp1+tmp2.substring(tmp1.length(),tmp2.length());
		}
		return tmp2;
		
	}
	public String decrypt(String text)
	{
		if (text==null || text.length()<=2) return text;
		String tmp1,tmp2;
		tmp2=text;
		for (int i=answerOfTheWorld-1;i>=0;i--)
		{
			tmp1="";
			if (i%2==1) tmp1+=tmp2.charAt(0);
			for (int j=0;j<(text.length()+1)/2-1;j++)
			{
				tmp1=tmp1+dehash(tmp2.charAt(j*2+1+i%2));
				tmp1=tmp1+dehash(tmp2.charAt(j*2+i%2));
			}
			tmp2=tmp1+tmp2.substring(tmp1.length(),tmp2.length());
		}
		return tmp2;
	}
}

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
		return textEncryptor.decrypt(super.getText());
	}
	@Override
	public void setText(String text) {
		// Encrypt
		if (init) super.setText(textEncryptor.encrypt(text));
		else super.setText(text);
		init=true;
		
	}
	private Encryptor textEncryptor=new Encryptor();
	private boolean init=false;

}
