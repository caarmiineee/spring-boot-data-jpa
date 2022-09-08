package com.nanosoft.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
//@EqualsAndHashCode
@Entity
@Table(name = "tutorials")
public class Tutorial {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Getter
	@Setter
	private Long id;

	@Column(name = "title")
	@Getter
	@Setter
	private String title;

	@Column(name = "description")
	@Getter
	@Setter
	private String description;

	@Column(name = "published")
	@Getter
	@Setter
	private Boolean published;

	@Column(name = "level")
	@Getter
	@Setter
	private Integer level;

	@Temporal(TemporalType.TIMESTAMP)
	@Getter
	@Setter
	private Date createdAt;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "tutorial_id")
	@Getter
	@Setter
	@JsonIgnore
	private Set<Comment> comments = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "tutorial_tags", joinColumns = { @JoinColumn(name = "tutorial_id") }, inverseJoinColumns = {
			@JoinColumn(name = "tag_id") })
	private Set<Tag> tags = new HashSet<>();

	public Tutorial(String title, String description, Boolean published, Set<Comment> comments, Set<Tag> tags) {
		super();
		this.title = title;
		this.description = description;
		this.published = published;
		this.comments = comments;
		this.tags = tags;
	}

	public void addTag(Tag tag) {
		this.tags.add(tag);
		tag.getTutorials().add(this);
	}

	public void removeTag(long tagId) {
		Tag tag = this.tags.stream().filter(t -> t.getId() == tagId).findFirst().orElse(null);
		if (tag != null) {
			this.tags.remove(tag);
			tag.getTutorials().remove(this);
		}
	}

}
