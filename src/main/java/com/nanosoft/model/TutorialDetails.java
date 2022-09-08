package com.nanosoft.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "tutorial_details")
public class TutorialDetails {

	@Id
	@Getter @Setter
	private Long id;

	@Column
	@Getter @Setter
	private Date createdOn;

	@Column
	@Getter @Setter
	private String createdBy;

	@OneToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(name = "tutorial_id")
	@Getter @Setter
	private Tutorial tutorial;

	public TutorialDetails(String createdBy) {
		this.createdOn = new Date();
		this.createdBy = createdBy;
	}

}