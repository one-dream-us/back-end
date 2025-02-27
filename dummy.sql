INSERT INTO content (title,
                     content_url,
                     thumbnail_url,
                     created_at)
VALUES ('올해만 544% 상승한 지구상에서 가장 뜨거운 주식.',
        'https://www.youtube.com/watch?v=HO7AsTjY7Bs',
        'https://img.youtube.com/vi/HO7AsTjY7Bs/mqdefault.jpg',
        NOW());

INSERT INTO content (title,
                     content_url,
                     thumbnail_url,
                     created_at)
VALUES ('테슬라 SOXL 애플 이번주 기대가 되는 이유!',
        'https://www.youtube.com/watch?v=Ds63BSxmGF8',
        'https://img.youtube.com/vi/Ds63BSxmGF8/mqdefault.jpg',
        NOW());


INSERT INTO script_full (content_id)
VALUES (1);

INSERT INTO script_full (content_id)
VALUES (3);

INSERT INTO script_paragraph (timestamp, script_full_id, paragraph_text)
VALUES ('00:00:00', 1,
        '자 오늘은 지구상에서 가장 뜨거운 주식에 대해서 얘기를 해 볼 건데 매수매도에 대한 추천 절대 아니라는 거. 일단 오늘의 주인공은 바로 <mark>마이크로스트레티지</mark>라는 주식입니다. 주가가 벌써 516%가 올해 올랐고요. 지금 제가 영상 이제 찍고 나서 주가를 확인해 보니까 오늘도 화끈하게 9% 상승을 하고 있습니다.');

INSERT INTO script_paragraph (timestamp, script_full_id, paragraph_text)
VALUES ('00:00:21', 1,
        '제가 토스 증권에 들어가 봤는데, 참고로 PPL 아니고요. 근데 이제 거래대금 순위를 1위부터 5위까지 제가 보니까 1위가 MSTU, 2위가 리게티 컴퓨팅 (처음 들어보는 회사인데 어쨌든), 3위가 퀀텀 컴퓨팅 그리고 4위가 MSTX, 5위가 <mark>마이크로스트레티지</mark>죠.');

INSERT INTO script_paragraph (timestamp, script_full_id, paragraph_text)
VALUES ('00:00:42', 1,
        '요렇게만 보면 아니 마이크로스트래티지 5위인데,  "김단태님 도대체 이거 왜 하나요?" 라고 물어볼 수가 있겠지만, 여러분들 자세히 보시면 MSTU 뭐냐! 마이크로스트티지를 두 배짜리, 두 배해서 사는 그런 ETF구요. 그러니까 이제 마이크로스트레티지가 10% 오르면, 이 ETF는 20%가 오르는 겁니다.');

INSERT INTO script_paragraph (timestamp, script_full_id, paragraph_text)
VALUES ('00:01:07', 1,
        '그리고 MSTX 사실상 같은 거예요. 그래서 하루에 마이크로스트레티지 움직임에 두 배씩 움직이는 그런 <mark>ETF</mark>입니다. 그러니까 사실은 1위, 4위, 5위가 전부 마이크로스트레티지라는거예요. 아 이거 뭐 이렇게 뜨거운 주식인데 안 할 수가 있나. 물론 제가 이제 예전에도 많이 다루긴 했었지만.');
----
INSERT INTO script_paragraph (timestamp, script_full_id, paragraph_text)
VALUES ('00:00:00', 2,
        '네 안녕하세요 2024년 12월 2일 월요일 킴스 미국 주식 시작하겠습니다.');

INSERT INTO script_summary (summary_text, content_id)
VALUES ('우주에선 안 뜨겁다!', 3);

INSERT INTO tag (value)
VALUES ('주식');

INSERT INTO tag (value)
VALUES ('다좋은데유튜버발음좀');

INSERT INTO content_tag (content_id, tag_id)
VALUES (9, 7);

INSERT INTO content_view (content_id, view_count)
VALUES (1, 1);

INSERT INTO content_view (content_id, view_count)
VALUES (2, 1);

INSERT INTO content_view (content_id, view_date, view_count)
VALUES (3, NOW(), 1)
ON CONFLICT (content_id)
    DO UPDATE SET view_count = content_view.view_count + 13999,
                  view_date  = NOW();
UPDATE content_view
SET view_count = view_count + 13999
WHERE content_id = 3;

INSERT INTO content_scrap (created_at, updated_at, content_id, user_id)
VALUES (NOW(), NOW(), 13, 3);

INSERT INTO dictionary_scrap (created_at, updated_at, dictionary_id, user_id)
VALUES (NOW(), NOW(), 1, 1);

UPDATE content
SET author = '내일은 투자왕 - 김단테'
WHERE id = 1;

DELETE
FROM content_tag
WHERE id = 10;

INSERT INTO dictionary (term, details)
VALUES ('마이크로스트레티지', '비트코인을 대량 보유한 기업으로 유명한 기업분석 소프트웨어 회사'),
       ('MSTR', '마이크로스트레티지의 주식 심볼');

INSERT INTO dictionary (term, details)
VALUES ('ETF', '상장지수펀드(Exchange Traded Fund)의 약자로, 특정 지수나 자산의 가격 변동을 추적하여 거래되는 펀드');

-- 첫 번째 문단에 대한 용어 매핑
INSERT INTO script_paragraph_dictionary (script_paragraph_id, dictionary_id)
SELECT 1, id
FROM dictionary
WHERE term = '마이크로스트레티지';

-- 5번 문단에 ETF 용어 매핑
INSERT INTO script_paragraph_dictionary (script_paragraph_id, dictionary_id)
SELECT 5, id
FROM dictionary
WHERE term = 'ETF';

INSERT INTO dictionary_scrap (created_at, updated_at, dictionary_id, user_id)
VALUES (NOW(), NOW(), 3, 5);

UPDATE dictionary
SET details = '마이크로스트레티지의 주식을 2배로 추종해서 거래하는 ETF'
WHERE id = 2;

UPDATE script_paragraph
SET paragraph_text = '제가 토스 증권에 들어가 봤는데, 참고로 PPL 아니고요. 근데 이제 거래대금 순위를 1위부터 5위까지 제가 보니까 1위가 <mark>MSTU</mark>, 2위가 리게티 컴퓨팅 (처음 들어보는 회사인데 어쨌든), 3위가 퀀텀 컴퓨팅 그리고 4위가 MSTX, 5위가 마이크로스트레티지죠.'
WHERE id = 2;

INSERT INTO content_history (content_id, user_id)
VALUES (2, 5);

INSERT INTO dictionary_scrap (created_at, updated_at, dictionary_id, user_id, is_deleted)
VALUES (NOW(), NOW(), 1, 3, false);

INSERT INTO content (content_url,
                     created_at,
                     thumbnail_url,
                     title,
                     author)

INSERT content_tag (content_id, tag_id)
VALUES (1, 1);


UPDATE script_summary
SET summary_text = '<p>"야수의 심장으로..판단은 냉정하게"<br/>
마이크로스트레티지는 비트코인을 대규모로 보유한 독특한 투자처로 자리 잡고 있는 회사로, 올해만 주가가 516% 오르고 있어요. <br/> 하지만 과대평가 논란이 있는 상황이라 리스크와 기회를 고려한 투자 전략이 필요해 보입니다.</p>'
WHERE content_id = 1;

UPDATE script_paragraph
SET paragraph_text='<p>그리고 MSTX도 사실상 같은 거예요. 그래서 하루에 마이크로스트레티지 움직임에 두 배씩 움직이는 그런 ETF입니다. 그러니까 사실은 1위, 4위, 5위가 전부 마이크로스트레티지라는거예요. 아 이거 뭐 이렇게 뜨거운 주식인데 안 할 수가 있나. 물론 제가 이제 예전에도 많이 다루긴 했었지만.</p>'
WHERE id = 5;

INSERT INTO script_paragraph (timestamp, script_full_id, paragraph_text)
VALUES ('00:10:10', 1, '<p>자 그래서 이렇게 부정적으로 볼 수도 있고 분명히 비트코인 가격이 하락하는 경우에는 또 더 크게 하락할 수 있기 때문에, "우리가 조심히 이런 주식을 바라봐야 된다."라고 생각을 하고요. 다시 한 번 강조 드릴게요. 매수매도에 대한 추천은 아닙니다. 다만 이 사건이 이 상황이 어떻게 흘러가는지에 대해서도 계속 다뤄 보도록 하겠습니다. 감사합니다.</p>');

UPDATE dictionary
SET details = 'ETF는 여러 주식이나 자산을 한데 모아 만든 펀드를 주식처럼 거래하는 상품이에요. 간편하게 분산투자를 할 수 있는 장점이 있어요.'
WHERE id = 3;

INSERT INTO dictionary (term, details)
VALUES ('전환사채', '전환사채(CB)는 채권 형태로 발행되지만 일정 조건에서 발행 회사의 주식으로 바꿀 수 있는 채권이에요. 안정성과 성장 가능성을 동시에 가진 상품이에요.');

insert into content_tag (content_id, tag_id)
values (1, 9);

ALTER TABLE script_paragraph ALTER COLUMN paragraph_text TYPE text;

insert into script_paragraph_dictionary
    (dictionary_id, script_paragraph_id)
values (16, 21);