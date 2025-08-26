package stage.suspectdetection;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import stage.suspectdetection.controller.CasSuspectController;
import stage.suspectdetection.entities.CasSuspect;
import stage.suspectdetection.service.CasSuspectService;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CasSuspectControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CasSuspectService casSuspectService;

    @InjectMocks
    private CasSuspectController casSuspectController;

    private CasSuspect cas1;
    private CasSuspect cas2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(casSuspectController).build();

        cas1 = new CasSuspect();
        cas1.setId(1L);
        cas1.setDateDetection(LocalDate.now());
        cas1.setScoreSimilaritee(0.9);

        cas2 = new CasSuspect();
        cas2.setId(2L);
        cas2.setDateDetection(LocalDate.now());
        cas2.setScoreSimilaritee(0.85);
    }

    @Test
    void testGetAllCases() throws Exception {
        when(casSuspectService.getAllCases()).thenReturn(Arrays.asList(cas1, cas2));

        mockMvc.perform(get("/api/cas-suspects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(cas1.getId()))
                .andExpect(jsonPath("$[1].id").value(cas2.getId()));

        verify(casSuspectService, times(1)).getAllCases();
    }
}
