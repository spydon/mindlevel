package net.mindlevel.client.widgets;

import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.services.QuoteService;
import net.mindlevel.client.services.QuoteServiceAsync;
import net.mindlevel.shared.Quote;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class NotFoundElement extends Composite {

    private final SimplePanel p;

    private final QuoteServiceAsync quoteService = GWT
            .create(QuoteService.class);

    private String notFoundText = "";

    private Canvas canvas;
    private Canvas backBuffer;

    private int mouseStartX = 300;
    private int mouseStartY = 120;

    // mouse positions relative to canvas
    private int mouseX = mouseStartX;
    private int mouseY = mouseStartY;

    //timer refresh rate, in milliseconds
    private static final int refreshRate = 25;

    // canvas size, in px
    private int height = 450;
    private int width = 600;

    private Context2d context;
    private Context2d backBufferContext;

    private final Image image;

    public NotFoundElement() {
        p = new SimplePanel();
        image = new Image(Mindlevel.PATH + "images/notfound.jpg");
        int clientWidth = Window.getClientWidth();
        int clientHeight = Window.getClientHeight()-50;

        if(width > clientWidth || height > clientHeight) {
            int nativeWidth = width;
            int nativeHeight = height;
            if(width > clientWidth) {
                width = clientWidth;
                height = height*width/nativeWidth;

            } else if(height > clientHeight) {
                height = clientHeight;
                width = width*height/nativeHeight;
            }
            mouseStartX = mouseStartX*width/nativeWidth;
            mouseStartY = mouseStartY*height/nativeHeight;
            mouseX = mouseStartX;
            mouseY = mouseStartY;
        }

        init();
        // All composites must call initWidget() in their constructors.
        initWidget(p);
    }

    private void init() {
        final LoadingElement l = new LoadingElement(LoadingElement.SIZE.LONG);
        p.add(l);

        canvas = Canvas.createIfSupported();
        backBuffer = Canvas.createIfSupported();

        if (canvas == null) {
            l.removeFromParent();
            p.add(new Image(Mindlevel.PATH + "images/fallback.jpg"));
            return;
        }

        // init the canvases
        canvas.setWidth(width + "px");
        canvas.setHeight(height + "px");
        canvas.setCoordinateSpaceWidth(width);
        canvas.setCoordinateSpaceHeight(height);
        backBuffer.setCoordinateSpaceWidth(width);
        backBuffer.setCoordinateSpaceHeight(height);
        context = canvas.getContext2d();
        backBufferContext = backBuffer.getContext2d();

        final ImageElement imageElement = ImageElement.as(image.getElement());
        image.addLoadHandler(new LoadHandler() {

            @Override
            public void onLoad(LoadEvent event) {
                l.removeFromParent();
                p.add(canvas);
                imageElement.setWidth(width);
                imageElement.setHeight(height);
                context.drawImage(imageElement, 0, 0, width, height);
                backBufferContext.drawImage(imageElement, 0, 0, width, height);
                loadQuote();
            }
        });
        image.setVisible(false);
        RootPanel.get().add(image);

        // setup timer
        final Timer timer = new Timer() {
            @Override
            public void run() {
                drawText();
            }
        };

        canvas.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                timer.scheduleRepeating(refreshRate);
            }
        });

        canvas.addMouseMoveHandler(new MouseMoveHandler() {
            @Override
            public void onMouseMove(MouseMoveEvent event) {
                mouseX = event.getRelativeX(canvas.getElement());
                mouseY = event.getRelativeY(canvas.getElement());
            }
        });

        canvas.addMouseOutHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                timer.cancel();
                mouseX = mouseStartX;
                mouseY = mouseStartY;
                drawText();
            }
        });

        canvas.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                loadQuote();
            }
        });
    }

    private void loadQuote() {
        quoteService.getNotFound(new AsyncCallback<Quote>() {

            @Override
            public void onSuccess(Quote quote) {
                if(width < 600) {
                    notFoundText = "Not found.";
                    drawText();
                } else if(!notFoundText.equals(quote.toString())) {
                    notFoundText = quote.toString();
                    drawText();
                } else {
                    loadQuote();
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
            }
        });
    }

    private void drawText() {
        context.drawImage(backBufferContext.getCanvas(), 0, 0);
        int textWidth = 280;
        int textHeight = 30;
        context.setFillStyle("rgba(255,255,255,.6)");

        context.save();
        context.scale(6, 1);
        context.beginPath();
        context.arc(mouseX/6+(textWidth/12), mouseY-5, textHeight, 0, 2 * Math.PI, false);
        context.closePath();
        context.restore();
        context.fill();

        drawCircle(50, 10, 1.5, 1.5, 14);
        drawCircle(70, 30, 2.5, 2.5, 10);
        drawCircle(60, 50, 5, 5, 6);

        context.setFont("bold 14px helvetica");
        context.setFillStyle("black");
        context.fillText(notFoundText, mouseX, mouseY);
    }

    private void drawCircle(int diffX, int diffY, double slowX, double slowY, int radius) {
        context.beginPath();
        context.arc((mouseStartX-diffX)-(mouseStartX-mouseX)/slowX, (mouseStartY+diffY)-(mouseStartY-mouseY)/slowY, radius, 0, 2 * Math.PI, false);
        context.closePath();
        context.restore();
        context.fill();
    }
}