package com.onedreamus.project.bank.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "script_summary")
public class ScriptSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "summary_text")
	private String summaryText;

	@Column(name = "content_id")
	private Long contentId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id", insertable = false, updatable = false)
	private Content content;
}
