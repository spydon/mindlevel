package net.mindlevel.client.pages;

import java.util.ArrayList;

import org.cobogw.gwt.user.client.ui.Rating;

import net.mindlevel.client.HandyTools;
import net.mindlevel.client.Mindlevel;
import net.mindlevel.client.services.MetaUploadService;
import net.mindlevel.client.services.MetaUploadServiceAsync;
import net.mindlevel.client.services.MissionService;
import net.mindlevel.client.services.MissionServiceAsync;
import net.mindlevel.client.services.PictureService;
import net.mindlevel.client.services.PictureServiceAsync;
import net.mindlevel.client.services.RatingService;
import net.mindlevel.client.services.RatingServiceAsync;
import net.mindlevel.shared.MetaImage;
import net.mindlevel.shared.Mission;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Picture {
	private RootPanel appArea;
	private final Image image = new Image("../images/loading.gif");
	private final Image leftArrow = new Image("../images/icons/left2.svg");
	private final Image rightArrow = new Image("../images/icons/right2.svg");
	private int id = 0;
	private int realId = 1;
	private int imageCount = Integer.MAX_VALUE;
	private boolean validated = true;
	private boolean notFound = false;
	private boolean activeArrow = false;
	private HTML title, description, location, owner, tags, date, mission, category, link, score;
	private Canvas keyUpHack;
	private VerticalPanel ratingPanel = new VerticalPanel();
	private VerticalPanel metaPanel;
	private Button validate = new Button("Validate");
	private Button delete = new Button("Delete");
	private Rating rating = new Rating(0,5,1,"../images/star.png","../images/stardeselected.png","../images/starhover.png",32,32);

	/**
	 * Create a remote service proxy to talk to the server-side picture
	 * service.
	 */
	private final PictureServiceAsync pictureService = GWT
			.create(PictureService.class);
	
	/**
	 * Create a remote service proxy to talk to the server-side mission
	 * service.
	 */
	private final MissionServiceAsync missionService = GWT
			.create(MissionService.class);
	
	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final RatingServiceAsync ratingService = GWT
			.create(RatingService.class);

	public Picture(RootPanel appArea, int id, boolean validated) {
		this.appArea = appArea;
		this.id = id;
		this.validated = validated;
		title = new HTML();
		location = new HTML();
		owner = new HTML();
		description = new HTML();
		mission = new HTML();
		category = new HTML();
		tags = new HTML();
		score = new HTML();
		link = new HTML();
		date = new HTML();
		image.addStyleName("missionPicture");
		title.addStyleName("pictureTitle");
		location.addStyleName("pictureInfo");
		description.addStyleName("pictureDescription");
		tags.addStyleName("pictureInfo");
		owner.addStyleName("pictureInfo");
		category.addStyleName("pictureInfo");
		mission.addStyleName("pictureInfo");
		score.addStyleName("scoreInfo");
		link.addStyleName("pictureInfo");
		date.addStyleName("pictureInfo");
		Window.enableScrolling(true);
		init();
	}

	private void init() {
		loadImage(id, false);
		if(HandyTools.isLoggedIn() && validated) {
			getVoteValue();
			rating.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					ratingService.setVoteValue(Mindlevel.user.getToken(), realId, rating.getValue(), new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable caught) {
							HandyTools.showDialogBox("Error", new HTML("The voting doesn't work at the moment, please try again later." + caught.getMessage()));
						}

						@Override
						public void onSuccess(Void result) {
							getVoteValue();
						}
					});
				}
			});
		}
		HorizontalPanel picturePanel = new HorizontalPanel();
		// imagePanel.setHeight("100%");
		picturePanel.setStylePrimaryName("picturePanel");
		image.setStylePrimaryName("picture");
		leftArrow.setStylePrimaryName("arrow-left");
		rightArrow.setStylePrimaryName("arrow-right");
//		leftArrow = new HTML("<div id=\"arrow-left\"></div>");
//		rightArrow = new HTML("<div id=\"arrow-right\"></div>");
		picturePanel.add(leftArrow);
		picturePanel.add(image);
		picturePanel.add(rightArrow);
		leftArrow.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				activeArrow = true;
			}
		});
//		leftArrow.addMouseOutHandler(new MouseOutHandler() {
//			@Override
//			public void onMouseOut(MouseOutEvent event) {
//				activeArrow = false;
//			}
//		});
		leftArrow.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				prevImage();
				activeArrow = false;
			}
		});
		rightArrow.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				activeArrow = true;
			}
		});
//		rightArrow.addMouseOutHandler(new MouseOutHandler() {
//			@Override
//			public void onMouseOut(MouseOutEvent event) {
//				activeArrow = false;
//			}
//		});
		rightArrow.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				nextImage();
				activeArrow = false;
			}
		});
		ImageHandler imageHandler = new ImageHandler();
		image.addClickHandler(imageHandler);
		keyUpHack = Canvas.createIfSupported();
		keyUpHack.setSize("0px", "0px");
		keyUpHack.addKeyUpHandler(imageHandler);
		keyUpHack.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if(!Mindlevel.user.isAdmin() && !activeArrow)
					arrowFocus();
				else if(activeArrow)
					activeArrow = false;
			}
		});
		metaPanel = new VerticalPanel();
		metaPanel.addStyleName("cardpanel");
		HorizontalPanel alignPanel = new HorizontalPanel();
		alignPanel.setStyleName("metapanel");
		VerticalPanel infoPanel = new VerticalPanel();
		infoPanel.addStyleName("infoPanel");
		infoPanel.add(location);
		infoPanel.add(owner);
		infoPanel.add(mission);
		infoPanel.add(category);
		infoPanel.add(tags);
		infoPanel.add(link);
		infoPanel.add(date);
		alignPanel.add(description);
		alignPanel.add(infoPanel);

		//Get rid of this somehow
		VerticalPanel centerHack = new VerticalPanel();
		if(HandyTools.isLoggedIn() && validated)
			ratingPanel.add(rating);
		centerHack.add(ratingPanel);
		centerHack.add(score);
		metaPanel.add(centerHack);
		metaPanel.add(alignPanel);
		if(Mindlevel.user.isAdmin()) {
			validate.addStyleName("smallmargin");
			delete.addStyleName("smallmargin");
			if(!validated)
				metaPanel.add(validate);
			metaPanel.add(delete);
		}
		appArea.add(title);
		appArea.add(picturePanel);
		appArea.add(metaPanel);
		appArea.add(keyUpHack);
		arrowFocus();
	}

	class ImageHandler implements ClickHandler, KeyUpHandler {
		/**
		 * Fired when the user clicks on the sendButton.
		 */
		public void onClick(ClickEvent event) {
			randomImage();
		}

		/**
		 * Fired when the user types in the nameField.
		 */
		public void onKeyUp(KeyUpEvent event) {
			if(!notFound)
				if (event.getNativeKeyCode() == KeyCodes.KEY_RIGHT && id < imageCount)
					nextImage();
				else if (event.getNativeKeyCode() == KeyCodes.KEY_LEFT && id > 1)
					prevImage();
				else if (event.getNativeKeyCode() == (int)'R')
					randomImage();
				else if (event.getNativeKeyCode() == (int)'H')
					HandyTools.showDialogBox("Shortcuts", new HTML("Right/Left Arrow - Browse pictures</br>R - Random picture</br>H - Show this help"));
		}
	}
	
	private void randomImage() {
		loadImage(-1, true);
		clearFields();
		arrowFocus();
	}

	private void nextImage() {
		if (id < imageCount)
			loadImage(++id, true);
		arrowFocus();
	}

	private void prevImage() {
		if (id > 1)
			loadImage(--id, true);
		arrowFocus();
	}
	
	private void arrowFocus() {
	    /** A timer to make setFocus after a blurEvent possible. */
		Timer t = new Timer() {
			@Override
			public void run() {
				if(Mindlevel.forceFocus)
					keyUpHack.setFocus(true);
			}
		};
		t.schedule(0);
	}

	private void loadImage(final int id, final boolean relative) {
		setImageUrl("../images/loading.gif");
		pictureService.get(id, relative, validated, new AsyncCallback<MetaImage>() {
			public void onFailure(Throwable caught) {
				setImageUrl("../images/notfound.jpg");
				metaPanel.setVisible(false);
				leftArrow.setVisible(false);
				rightArrow.setVisible(false);
				title.setVisible(false);
				notFound = true;
			}

			public void onSuccess(final MetaImage metaImage) {
				ratingPanel.setVisible(true);
				setImageUrl("../pictures/" + metaImage.getFilename());
				imageCount = metaImage.getImageCount();
				if(id == 0)
					setId(imageCount);
				else if(!relative)
					setId(metaImage.getRelativeId());
				
				//Check if the left arrow is needed
				if (getId() == 1)
					leftArrow.addStyleName("hidden");
				else if(leftArrow.getStyleName().contains("hidden"))
					leftArrow.removeStyleName("hidden");
				
				//Check if the right arrow is needed
				if (getId() == imageCount)
					rightArrow.addStyleName("hidden");
				else if(rightArrow.getStyleName().contains("hidden"))
					rightArrow.removeStyleName("hidden");
				
				//If it is a 'notfound' picture
				if (imageCount == 0) {
					leftArrow.setVisible(false);
					rightArrow.setVisible(false);
					clearFields();
				}
				
				realId = metaImage.getId();
				if(validated) {
					getVoteValue();
				} else {
					validate.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							final MetaUploadServiceAsync metaUploadService = GWT
									.create(MetaUploadService.class);
							metaImage.setToken(Mindlevel.user.getToken());
							metaUploadService.upload(metaImage, true, new AsyncCallback<String>() {

								@Override
								public void onFailure(Throwable caught) {
									HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
								}

								@Override
								public void onSuccess(String result) {
									deletePicture(metaImage);
								}
							});
						}
					});
				}
				if(Mindlevel.user.isAdmin()) {
					delete.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) {
							//TODO
							//Add questionbox
							pictureService.deletePicture(metaImage.getId(), validated, Mindlevel.user.getToken(), new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable caught) {
									HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
								}

								@Override
								public void onSuccess(Void result) {
									HandyTools.showDialogBox("Success", new HTML("Successfully deleted!"));											
								}
							});							
						}
					});
				}
				title.setHTML(metaImage.getTitle());
				location.setHTML("<b>Location: </b>" + metaImage.getLocation());
				owner.setHTML("<b>Owner: </b>" + getAnchor("user", metaImage.getOwner(), metaImage.getOwner()));
				description.setHTML("<h1>Description</h1>"
						+ metaImage.getDescription());
				tags.setHTML(buildTagHTML(metaImage.getTags()));
				date.setHTML("<b>Creation date: </b>" + metaImage.getDate());
				if(validated)
					link.setHTML("<b>Link: </b>" + getAnchor("picture", Integer.toString(realId), "Right click to copy"));
				else
					link.setHTML("<b>Link: </b><a href=./Mindlevel.html?picture="+realId+"&validated=false>Right click to copy</a>");
				fetchMission(metaImage.getMissionId());
				getScore(realId);
			}
		});
	}
	
	private String buildTagHTML(ArrayList<String> tags) {
		String tagHtml = "<b>Tags: </b>";
		if(tags!=null)
			for(String tag : tags) {
				tagHtml = tagHtml.concat(getAnchor("user", tag, tag));
				if(tags.get(tags.size()-1)!=tag)
					tagHtml = tagHtml.concat(",&nbsp;");
			}
		return tagHtml;
	}
	
	private void setImageUrl(final String url) {
		final Image tmpImage = new Image();
		tmpImage.addLoadHandler(new LoadHandler() {
			
			@Override
			public void onLoad(LoadEvent event) {
				int width = (int)(Window.getClientWidth()*0.7);
				if(tmpImage.getWidth() > width)
					image.setPixelSize(width, tmpImage.getHeight()*width/tmpImage.getWidth());
				else
					image.setPixelSize(tmpImage.getWidth(), tmpImage.getHeight());
				image.setUrl(url);
			}
		});
		tmpImage.setVisible(false);
		appArea.add(tmpImage);
		tmpImage.setUrl(url);
	}
	
	private String getAnchor(String type, String data, String name) {
		return data!=null ? "<a href=./Mindlevel.html?"+type+"="+data+">"+name+"</a>" : "";
	}
	
	private void clearFields() {
		title.setHTML("");
		description.setHTML("");
		location.setHTML("");
		tags.setHTML("");
		owner.setHTML("");
		category.setHTML("");
		mission.setHTML("");
		link.setHTML("");
		score.setHTML("");
		date.setHTML("");
		ratingPanel.setVisible(false);
		validate.setVisible(false);
		delete.setVisible(false);
	}
	
	private void getVoteValue() {
		ratingService.getVoteValue(Mindlevel.user.getUsername(), realId, new AsyncCallback<Integer>() {
			@Override
			public void onFailure(Throwable caught) {
				HandyTools.showDialogBox("Error", new HTML("Something went wrong while loading the votes."));
			}

			@Override
			public void onSuccess(Integer result) {
				if(result != 0) {
					rating.setValue((int)result);
					rating.setReadOnly(true);
				} else {
					rating.setValue(0);
					rating.setReadOnly(false);
				}
			}
		});
	}
	
	private void fetchMission(int id) {
		missionService.getMission(id, true, new AsyncCallback<Mission>() {
			@Override
			public void onFailure(Throwable caught) {
				HandyTools.showDialogBox("Error", new HTML("Something went wrong while loading the mission information."));
			}

			@Override
			public void onSuccess(Mission m) {
				mission.setHTML("<b>Mission: </b>" + getAnchor("mission", Integer.toString(m.getId()), m.getName()));
				category.setHTML("<b>Category: </b>" + getAnchor("category", m.getCategory(), m.getCategory()));
			}
		});
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	private void getScore(final int id) {
		ratingService.getScore(id, new AsyncCallback<Double>() {

			@Override
			public void onFailure(Throwable caught) {
				score.setHTML("<b>Score: </b> No votes yet.");
			}

			@Override
			public void onSuccess(final Double totalScore) {
				ratingService.getVoteNumber(id, new AsyncCallback<Integer>() {

					@Override
					public void onFailure(Throwable caught) {
						score.setHTML("<b>Score: </b> No votes yet.");
					}

					@Override
					public void onSuccess(Integer votes) {
						score.setHTML("<b>Score: </b>" + totalScore + "/5 of " + votes + " votes");
					}
				});
			}
		});
	}
	
	private void deletePicture(final MetaImage metaImage) {
		pictureService.deleteTags(metaImage.getId(), validated, Mindlevel.user.getToken(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
			}

			@Override
			public void onSuccess(Void result) {
				pictureService.deletePicture(metaImage.getId(), validated, Mindlevel.user.getToken(), new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						HandyTools.showDialogBox("Error", new HTML(caught.getMessage()));
					}

					@Override
					public void onSuccess(Void result) {
						HandyTools.showDialogBox("Success", new HTML("Great success!"));											
					}
				});										
			}
		});
	}
}