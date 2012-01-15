package cl.skyvortex.rememberit;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

public class Remember extends Activity {
	private int width;
	private int height;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.height = this.getWindowManager().getDefaultDisplay().getHeight();
        this.width = this.getWindowManager().getDefaultDisplay().getWidth();
        setContentView(R.layout.main);
        displayItems();
        
    }
    
    private void displayItems(){
    	EditText date = (EditText)this.findViewById(R.id.edit_date);
    	date.setWidth(width/2);
    	
    	EditText time = (EditText)this.findViewById(R.id.edit_time);
    	time.setWidth(width/2);
    	
    	ToggleButton btnAM = (ToggleButton)this.findViewById(R.id.btn_am);
    	btnAM.setWidth(width/3);
    	
    	ToggleButton btnPM = (ToggleButton)this.findViewById(R.id.btn_pm);
    	btnPM.setWidth(width/3);
    	
    	ToggleButton btnAll = (ToggleButton)this.findViewById(R.id.btn_complete);
    	btnAll.setWidth(width/3);
    	
    	displayNumpad();
    }
    
    private void displayNumpad(){
    	Button one = (Button)this.findViewById(R.id.one);
    	one.setWidth(width/3);
    	one.setHeight(height/8);
    	
    	Button two = (Button)this.findViewById(R.id.two);
    	two.setWidth(width/3);
    	two.setHeight(height/8);
    	
    	Button three = (Button)this.findViewById(R.id.three);
    	three.setWidth(width/3);
    	three.setHeight(height/8);

    	Button four = (Button)this.findViewById(R.id.four);
    	four.setWidth(width/3);
    	four.setHeight(height/8);
    	
    	Button five = (Button)this.findViewById(R.id.five);
    	five.setWidth(width/3);
    	five.setHeight(height/8);
    	
    	Button six = (Button)this.findViewById(R.id.six);
    	six.setWidth(width/3);
    	six.setHeight(height/8);
    	
    	Button seven = (Button)this.findViewById(R.id.seven);
    	seven.setWidth(width/3);
    	seven.setHeight(height/8);
    	
    	Button eight = (Button)this.findViewById(R.id.eight);
    	eight.setWidth(width/3);
    	eight.setHeight(height/8);
    	
    	Button nine = (Button)this.findViewById(R.id.nine);
    	nine.setWidth(width/3);
    	nine.setHeight(height/8);
    	
    	Button zero = (Button)this.findViewById(R.id.zero);
    	zero.setWidth(width/3);
    	zero.setHeight(height/8);
    	
    	Button btnBack = (Button)this.findViewById(R.id.btn_back);
    	btnBack.setWidth(width/3);
    	btnBack.setHeight(height/8);
    	
    	Button btnOk = (Button)this.findViewById(R.id.btn_ok);
    	btnOk.setWidth(width/3);
    	btnOk.setHeight(height/8);
    }
}