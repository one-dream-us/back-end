package com.onedreamus.project.bank.model.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "script_summary")
@Entity(name="ScriptSummary")
public class ScriptSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "summary_text")
	private String summaryText;

//	@Column(name = "content_id")
//	private Long contentId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "content_id")
	private Content content;
}
