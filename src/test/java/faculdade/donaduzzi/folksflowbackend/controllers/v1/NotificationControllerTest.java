package faculdade.donaduzzi.folksflowbackend.controllers.v1;

import faculdade.donaduzzi.folksflowbackend.services.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Test
    @WithMockUser
    @DisplayName("Should list notifications for user successfully")
    void getNotificationsSuccess() throws Exception {
        when(notificationService.findAllByUser(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/notifications"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should get unread count successfully")
    void getUnreadCountSuccess() throws Exception {
        when(notificationService.countUnread(any())).thenReturn(5L);

        mockMvc.perform(get("/api/v1/notifications/unread/count"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Should mark notification as read successfully")
    void markAsReadSuccess() throws Exception {
        doNothing().when(notificationService).markAsRead(anyLong());

        mockMvc.perform(patch("/api/v1/notifications/1/read"))
                .andExpect(status().isNoContent());
    }
}
