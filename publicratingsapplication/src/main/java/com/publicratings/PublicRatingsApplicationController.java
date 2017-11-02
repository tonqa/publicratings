package com.publicratings;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.client.Traverson.TraversalBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

@Controller
public class PublicRatingsApplicationController {

	//GET FROM APPLICATION.PROPERTIES
	@Value( "${app.publicservice_root}" )
	private String serviceRoot;

	@RequestMapping(value={"", "/list"})
    public String index(Model model, RestTemplate rest, @RequestParam(value= "sort", required = false) String sort, @RequestParam(value= "favorites", required = false) Boolean favorites){

		String sortString ="";
		int rating;
		float pos;
		float neg;
		ArrayList<Place> filteredPlaces = new ArrayList<Place>();

		if (sort != null){
			sortString = "?sort="+sort;
		}

		//get all places
		Traverson traverson;
		ParameterizedTypeReference<Resources<Place>> resourceParameterizedTypeReference;
		Resources<Place> places;
		try {
			System.out.println("Beginning Request...");
			traverson = new Traverson(new URI(serviceRoot+"places"+sortString), MediaTypes.HAL_JSON);
			resourceParameterizedTypeReference =
			        new ParameterizedTypeReference<Resources<Place>>() {};
			places = traverson.//
			        follow().//
			        toObject(resourceParameterizedTypeReference);
			System.out.println("Request finished");
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			model.addAttribute("errorMessage", e);
			model.addAttribute("stackTrace", sw.toString());
			return "error";
		}

		for (Place place : places) {
			//Map id into Places
			String link = place.get_links().getSelf().getHref();
			String[] parts = link.split("/");
			String idString = parts[parts.length-1];
			place.setId(Long.parseLong(idString));

			//calc rating into Places
			neg = place.getClicksNeg();
			pos = place.getClicksPos();
			rating =(int) (pos/(neg+pos)*100);
			place.setRating(rating);

			//filter favorites
			if(favorites==null  || place.getIsFavorite()){
				filteredPlaces.add(place);
			}
		}

		if(sort==null){sort = "";}

		model.addAttribute("placeList", filteredPlaces);
		return "list";
    }

	@RequestMapping("/map")
    public String map(Model model, @RequestParam(value= "favorites", required = false) Boolean favorites) throws URISyntaxException {

		int rating;
		float pos;
		float neg;
		ArrayList<Place> filteredPlaces = new ArrayList<Place>();

		Traverson traverson;
		TraversalBuilder tb;
		ParameterizedTypeReference<Resources<Place>> typeRefDevices;
		Resources<Place> resPlaces;
		Collection<Place> places;
		try {
			System.out.println("Beginning Request...");
			traverson = new Traverson(new URI(serviceRoot+"places"), MediaTypes.HAL_JSON);
			tb = traverson.follow();
			typeRefDevices = new ParameterizedTypeReference<Resources<Place>>() {};
			resPlaces = tb.toObject(typeRefDevices);
			places= resPlaces .getContent();
			System.out.println("Request finished");
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			model.addAttribute("errorMessage", e);
			model.addAttribute("stackTrace", sw.toString());
			return "error";
		}


		//Map id into Places
		for (Place place : places) {
			String link = place.get_links().getSelf().getHref();
			String[] parts = link.split("/");
			String idString = parts[parts.length-1];
			place.setId(Long.parseLong(idString));

			//calc rating into Places
			neg = place.getClicksNeg();
			pos = place.getClicksPos();
			rating =(int) (pos/(neg+pos)*100);
			place.setRating(rating);

			//filter favorites
			if(favorites==null || place.getIsFavorite()){
				filteredPlaces.add(place);
			}
		}

		model.addAttribute("placeList", filteredPlaces);
		return "map";
    }

	@GetMapping("/new")
    public String createView(Model model) {
		model.addAttribute("place", new Place());
        return "new";
    }

	@PostMapping("/new")
    public String createSubmit(Model model, @ModelAttribute Place place) {

		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Place> entity = new HttpEntity<Place>(place,headers);

		try{
			rest.postForEntity(serviceRoot+"places", entity, String.class);
		}catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			model.addAttribute("errorMessage", e);
			model.addAttribute("stackTrace", sw.toString());
			return "error";
		}

        return "redirect:/";
    }

	@GetMapping("/edit")
    public String edit(Model model, RestTemplate rest, @RequestParam("id") String id) {

		HttpEntity<String> entity = new HttpEntity<String>("");

		ResponseEntity<Place> response = rest.exchange(serviceRoot+"places/"+id, HttpMethod.GET, entity, Place.class);
		Place place = response.getBody();

		String link = place.get_links().getSelf().getHref();
		String[] parts = link.split("/");
		String idString = parts[parts.length-1];

		place.setId(Long.parseLong(idString));

		model.addAttribute("place", place);

        return "edit";
    }

	@PostMapping("/edit")
    public String editSubmit(Model model, @ModelAttribute Place place, RestTemplate rest, @RequestParam("id") String id) {
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Place> entity = new HttpEntity<Place>(place,headers);

		ResponseEntity<Place> response;

		try{
			response = rest.exchange(serviceRoot+"places/"+id,HttpMethod.PUT, entity, Place.class);
		}catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			model.addAttribute("errorMessage", e);
			model.addAttribute("stackTrace", sw.toString());
			return "error";
		}

        return "redirect:/detail?id="+id;
    }

	@RequestMapping("/fav")
    public String fav(Model model) {
        return "fav";
    }

	@RequestMapping("/detail")
    public String details(Model model, RestTemplate rest, @RequestParam("id") String id) throws URISyntaxException{

		//get place
		HttpEntity<String> entity = new HttpEntity<String>("");
		ResponseEntity<Place> response = rest.exchange(serviceRoot+"places/"+id, HttpMethod.GET, entity, Place.class);
		Place place = response.getBody();

		//add to model
		model.addAttribute("place", place);
		model.addAttribute("map", "https://www.google.com/maps/embed/v1/place&q="+place.getLongitude()+","+place.getLatitude());

		//calc rating into Places
		float neg = place.getClicksNeg();
		float pos = place.getClicksPos();
		int rating =(int) (pos/(neg+pos)*100);
		place.setRating(rating);

		place.setId(Long.parseLong(id));

        return "detail";
    }

	@RequestMapping("/delete")
    public String delete(Model model, RestTemplate rest, @RequestParam("id") String id){


		HttpEntity<String> entity = new HttpEntity<String>("");

		try{
			rest.exchange(serviceRoot+"places/"+id, HttpMethod.DELETE, entity, RequestError.class);
		}catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			model.addAttribute("errorMessage", e);
			model.addAttribute("stackTrace", sw.toString());
			return "error";
		}

		return "redirect:/";

    }

	@RequestMapping("/favor")
    public String favor(Model model, RestTemplate rest, @RequestParam("id") String id){

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>("{\"isFavorite\": 1}", headers);

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		rest.setRequestFactory(requestFactory);

		try{
			rest.exchange(serviceRoot+"places/"+id, HttpMethod.PATCH, entity, RequestError.class);
		}catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			model.addAttribute("errorMessage", e);
			model.addAttribute("stackTrace", sw.toString());
			return "error";
		}

		return "redirect:/";

    }

	@RequestMapping("/unfavor")
    public String unfavor(Model model, RestTemplate rest, @RequestParam("id") String id){

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>("{\"isFavorite\": 0}", headers);

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		rest.setRequestFactory(requestFactory);

		try{
			rest.exchange(serviceRoot+"places/"+id, HttpMethod.PATCH, entity, RequestError.class);
		}catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			model.addAttribute("errorMessage", e);
			model.addAttribute("stackTrace", sw.toString());
			return "error";
		}

		return "redirect:/";

    }

	@RequestMapping("/alerts")
    public String alerts(Model model, RestTemplate rest){

		Traverson traverson;
		ParameterizedTypeReference<Resources<Click>> resourceParameterizedTypeReference;
		Resources<Click> clicks;
		try {
			System.out.println("Beginning Request...");
			traverson = new Traverson(new URI(serviceRoot+"clicks"), MediaTypes.HAL_JSON);
			resourceParameterizedTypeReference =
			        new ParameterizedTypeReference<Resources<Click>>() {};
			clicks = traverson.//
			        follow().//
			        toObject(resourceParameterizedTypeReference);
			System.out.println("Request finished");
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			model.addAttribute("errorMessage", e);
			model.addAttribute("stackTrace", sw.toString());
			return "error";
		}

		model.addAttribute("clickList", clicks);

		return "alerts";

    }
}
