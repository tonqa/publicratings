package com.publicratings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
public class NotificationController {
	@Value( "${service.notifications.mail}" )
	private String staticNotifcationMail;

	@Autowired
    private NotificationService notificationService;

	@Autowired
    private PlaceRepository placeRepository;

	@Autowired
	private ClickRepository clickRepository;

	@RequestMapping(value="/places", method=RequestMethod.POST)
    public ResponseEntity<String> addPlaces(@RequestBody Place place){
		// save place
		placeRepository.save(place);
		// send a notification
		notificationService.sendNotification(staticNotifcationMail, "[PublicRatings] Neuer Ort", "Es wurde ein neuer Ort hinzugefügt: " + place.getName());

		HttpHeaders httpHeaders = new HttpHeaders();
	    return new ResponseEntity<>("{}", httpHeaders, HttpStatus.CREATED);
    }


	 @RequestMapping(value = "/clicks", method = RequestMethod.POST)
	 public @ResponseBody ResponseEntity<String> addClicks(
	        @RequestBody Resource<Click> clickResource,
	        PersistentEntityResourceAssembler assembler) {
		Click click = clickResource.getContent();
		clickRepository.save(click);
		Place place = click.getPlace();

		if (place.getIsFavorite() == 1) {
			if (click.getType() == 0)
				notificationService.sendNotification(staticNotifcationMail, "[PublicRatings] Favorit negativ bewertet", "Dein Favorit " + place.getName() + " hat eine schlechte Bewertung soeben erhalten.");
			if (click.getType() == 1)
				notificationService.sendNotification(staticNotifcationMail, "[PublicRatings] Favorit positiv bewertet", "Dein Favorit " + place.getName() + " hat eine gute Bewertung soeben erhalten.");
		}

	    HttpHeaders httpHeaders = new HttpHeaders();
	    return new ResponseEntity<>("{}", httpHeaders, HttpStatus.CREATED);
	}

	// TODO: "/notifications/clicks"
	 @RequestMapping(value = "/notifications/clicks", method = RequestMethod.GET)
	 public ResponseEntity<List<Place>> getNotifications(){
		List<Place> places = placeRepository.findByIsFavorite(1);

		// TODO: get clicks for places and return clicks with places

	    HttpHeaders httpHeaders = new HttpHeaders();
	    return new ResponseEntity<>(places, httpHeaders, HttpStatus.OK);
	}

	// TODO: "/notifications/places"

}