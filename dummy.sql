INSERT INTO content (title,
                     content_url,
                     thumbnail_url,
                     created_at)
VALUES ('올해만 544% 상승한 지구상에서 가장 뜨거운 주식.',
        'https://www.youtube.com/watch?v=HO7AsTjY7Bs',
        'https://img.youtube.com/vi/HO7AsTjY7Bs/mqdefault.jpg',
        NOW());

INSERT INTO script_full (content_id)
VALUES (LASTVAL());

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

INSERT INTO tag (value)
VALUES ('주식');

INSERT INTO content_tag (content_id, tag_id)
VALUES (1,1);

INSERT INTO content_view (content_id, viewed_at, view_count)
VALUES (1, NOW(), 1);

