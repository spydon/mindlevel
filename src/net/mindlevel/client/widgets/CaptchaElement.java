package net.mindlevel.client.widgets;

import net.mindlevel.client.services.CaptchaService;
import net.mindlevel.client.services.CaptchaServiceAsync;
import net.mindlevel.client.tools.HandyTools;
import net.mindlevel.shared.Captcha;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.mgwt.ui.client.widget.form.FormEntry;
import com.googlecode.mgwt.ui.client.widget.input.MTextBox;

public class CaptchaElement extends Composite {

    private final VerticalPanel b;
    private final HorizontalPanel p;
    private final Captcha captcha = new Captcha();
    private final TextBox tb = new TextBox();
    private final MTextBox mtb = new MTextBox();
    private final FormEntry formEntry = new FormEntry("", mtb);

    private final CaptchaServiceAsync captchaService = GWT
            .create(CaptchaService.class);

    /**
     * Constructs a captcha widget
     *
     */
    public CaptchaElement() {
        b = new VerticalPanel();
        p = new HorizontalPanel();
        b.add(new Label("Prove you're not a robot:"));
        b.add(p);
        tb.setWidth("50px");
        generate();
        // All composites must call initWidget() in their constructors.
        initWidget(b);
    }

    public FormEntry getFormEntry() {
        return formEntry;
    }

    public String getAnswer() {
        return tb.getText().equals("") ? mtb.getText() : tb.getText();
    }

    public String getToken() {
        return captcha.getToken();
    }

    public void generate() {
        captchaService.get(new AsyncCallback<Captcha>() {

            @Override
            public void onSuccess(Captcha newCaptcha) {
                captcha.setQuestion(newCaptcha.getQuestion());
                captcha.setToken(newCaptcha.getToken());
                Label question = new Label(captcha.getQuestion());
                question.addStyleName("captcha-question");
                p.add(question);
                p.add(tb);
                formEntry.setText(captcha.getQuestion());
            }

            @Override
            public void onFailure(Throwable arg0) {
                HandyTools.showDialogBox("Error", new HTML(arg0.getMessage()));
            }
        });
    }
}