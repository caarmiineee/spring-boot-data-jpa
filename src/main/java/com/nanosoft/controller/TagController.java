package com.nanosoft.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nanosoft.model.Tag;
import com.nanosoft.model.Tutorial;
import com.nanosoft.repository.TagRepository;
import com.nanosoft.repository.TutorialRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class TagController {

	@Autowired
	private TutorialRepository tutorialRepository;
	@Autowired
	private TagRepository tagRepository;

	@GetMapping("/tags")
	public ResponseEntity<List<Tag>> getAllTags() {

		List<Tag> tags = new ArrayList<>();
		tagRepository.findAll().forEach(tags::add);
		if (tags.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(tags, HttpStatus.OK);
	}

	@GetMapping("/tutorials/{tutorialId}/tags")
	public ResponseEntity<List<Tag>> getAllTagsByTutorialId(@PathVariable(value = "tutorialId") Long tutorialId) {

		if (!tutorialRepository.existsById(tutorialId)) {
			throw new IllegalArgumentException("Not found Tutorial with id = " + tutorialId);
		}
		List<Tag> tags = tagRepository.findTagsByTutorialsId(tutorialId);
		return new ResponseEntity<>(tags, HttpStatus.OK);
	}

	@GetMapping("/tags/{id}")
	public ResponseEntity<Tag> getTagsById(@PathVariable(value = "id") Long id) {

		Tag tag = tagRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Not found Tag with id = " + id));
		return new ResponseEntity<>(tag, HttpStatus.OK);
	}

	@GetMapping("/tags/{tagId}/tutorials")
	public ResponseEntity<List<Tutorial>> getAllTutorialsByTagId(@PathVariable(value = "tagId") Long tagId) {

		if (!tagRepository.existsById(tagId)) {
			throw new IllegalArgumentException("Not found Tag  with id = " + tagId);
		}
		List<Tutorial> tutorials = tutorialRepository.findTutorialsByTagsId(tagId);
		return new ResponseEntity<>(tutorials, HttpStatus.OK);
	}

	@PostMapping("/tutorials/{tutorialId}/tags")
	public ResponseEntity<Tag> addTag(@PathVariable(value = "tutorialId") Long tutorialId,
			@RequestBody Tag tagRequest) {

		Tag tag = tutorialRepository.findById(tutorialId).map(tutorial -> {
			long tagId = tagRequest.getId();

			// tag is existed
			if (tagId != 0L) {
				Tag _tag = tagRepository.findById(tagId)
						.orElseThrow(() -> new IllegalArgumentException("Not found Tag with id = " + tagId));
				tutorial.addTag(_tag);
				tutorialRepository.save(tutorial);
				return _tag;
			}

			// add and create new Tag
			tutorial.addTag(tagRequest);
			return tagRepository.save(tagRequest);
		}).orElseThrow(() -> new IllegalArgumentException("Not found Tutorial with id = " + tutorialId));
		return new ResponseEntity<>(tag, HttpStatus.CREATED);
	}

	@PutMapping("/tags/{id}")
	public ResponseEntity<Tag> updateTag(@PathVariable("id") long id, @RequestBody Tag tagRequest) {

		Tag tag = tagRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("TagId " + id + "not found"));
		tag.setName(tagRequest.getName());
		return new ResponseEntity<>(tagRepository.save(tag), HttpStatus.OK);
	}

	@DeleteMapping("/tutorials/{tutorialId}/tags/{tagId}")
	public ResponseEntity<HttpStatus> deleteTagFromTutorial(@PathVariable(value = "tutorialId") Long tutorialId,
			@PathVariable(value = "tagId") Long tagId) {

		Tutorial tutorial = tutorialRepository.findById(tutorialId)
				.orElseThrow(() -> new IllegalArgumentException("Not found Tutorial with id = " + tutorialId));

		tutorial.removeTag(tagId);
		tutorialRepository.save(tutorial);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/tags/{id}")
	public ResponseEntity<HttpStatus> deleteTag(@PathVariable("id") long id) {

		tagRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}