package net.xeill.elpuig.thinkitapp.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import net.xeill.elpuig.thinkitapp.R;

public class MathsTutorialActivity extends AppCompatActivity {
    ShowcaseView sv;
    int clickCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths);

        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);

        sv = new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .build();
        sv.setButtonPosition(lps);

        sv.overrideButtonClick(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                clickCount++;
                switch (clickCount) {
                    case 1:
                        sv.setTarget(new ViewTarget(R.id.op1_layout, MathsTutorialActivity.this));
                        sv.setContentTitle("Operación principal");
                        sv.setContentText("Esta es la operación que debes completar");
                        sv.setShowcaseX(300);
                        sv.setButtonText("next");
                        break;
                    case 2:
                        sv.setTarget(new ViewTarget(R.id.op2_layout, MathsTutorialActivity.this));
                        sv.setContentTitle("Siguiente operación");
                        sv.setContentText("¡Prepárate para la siguiente operación echándole un vistazo rápido!");
                        sv.setButtonText("next");
                        break;
                    case 3:
                        sv.setTarget(new ViewTarget(R.id.keyboard_line1, MathsTutorialActivity.this));
                        sv.setContentTitle("Respuestas posibles");
                        sv.setContentText("Observa las respuestas posibles, piensa bien y pulsa en la que creas correcta");
                        sv.setButtonText("next");
                        break;
                }
            }
        });
    }
}
