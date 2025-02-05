package com.onedreamus.project.global.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleSheetsService {

    @Value("${google.api.application-name}")
    private String APPLICATION_NAME; // google cloud 프로젝트 명
    @Value("${google.api.spreadsheets.id}")
    private String SPREADSHEET_ID; // 스프레드 시트 ID
    @Value("${google.api.spreadsheets.sheet-name}")
    private String SHEETS_NAME; // 사용할 스프레드 시트 이름
    @Value("${google.api.spreadsheets.range}")
    private String INSERT_RANGE; // 삽입 범위 지정

    private Sheets sheetsService;

    @PostConstruct
    public void init() throws IOException, GeneralSecurityException {
        // 서비스 계정 키 JSON 파일 경로
        String credentialsPath = "src/main/resources/credentials.json";

        // ServiceAccountCredentials 생성
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(
                        new FileInputStream(credentialsPath))
                .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

        // Sheets 서비스 인스턴스 생성
        sheetsService = new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * <p>구글 스프레드 시트 데이터 삽입</p>
     * 구글 스프레드 시트에 데이터를 삽입.
     * @param date
     * @param reviewValue
     * @param rating
     * @throws IOException
     */
    public void insertReviewData(LocalDate date, String reviewValue, int rating) throws IOException {
        List<Object> row = Arrays.asList(
                reviewValue,
                String.valueOf(rating),
                date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        );

        List<List<Object>> values = Collections.singletonList(row);

        ValueRange body = new ValueRange()
                .setValues(values);

        // ! 를 통해 스프레드 시트 이름과 range 값 구분
        String range = String.format("%s!%s", SHEETS_NAME, INSERT_RANGE);

        sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, range, body)
                .setValueInputOption("RAW")
                .execute();
    }
}
