package medical;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class MedicalServiceImplTest {

    private MedicalServiceImpl msi;

    @Mock
    private PatientInfoRepository patientInfoRepository;
    @Mock
    SendAlertService sendAlertService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        msi = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
    }

    @Test
    //проверяем работу метода с повышенным давлением
    public void checkBloodPressure_HihgPressure_Test() {
        //Задаем параметр id
        String patientId = "15";
        //создаем пациента с нормальными параметрами
        PatientInfo patientInfo = new PatientInfo("John", "Doe"
                , LocalDate.now(), new HealthInfo(BigDecimal.valueOf(36.6)
                , new BloodPressure(120, 80)));
        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);
        //имитируем поведение bloodPressure  завышенным давлением
        BloodPressure bloodPressure = new BloodPressure(150, 80);
        //запускаем метод checkBloodPressure
        msi.checkBloodPressure(patientId, bloodPressure);
        //проверяем что метод будет вызван только 1 раз
        verify(sendAlertService, only()).send(anyString());
    }

    @Test
    //проверяем метод с пониженным давлением
    public void checkBloodPressure_LowPressure_Test() {
        //Задаем параметр id
        String patientId = "15";
        //создаем пациента с нормальными параметрами
        PatientInfo patientInfo = new PatientInfo("John", "Do", LocalDate.now()
                , new HealthInfo(BigDecimal.valueOf(36.6), new BloodPressure(120, 80)));
        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);
        //имитируем поведение bloodPressure  пониженное давление
        BloodPressure bloodPressure = new BloodPressure(100, 70);
        //запускаем метод checkBloodPressure
        msi.checkBloodPressure(patientId, bloodPressure);
        //проверяем что метод будет вызван только 1 раз
        verify(sendAlertService, only()).send(anyString());
    }

    @Test
    //проверяем метод с пониженным давлением
    public void checkBloodPressure_NormalPressure_Test() {
        //Задаем параметр id
        String patientId = "15";
        //создаем пациента с нормальными параметрами
        PatientInfo patientInfo = new PatientInfo("John", "Do", LocalDate.now()
                , new HealthInfo(BigDecimal.valueOf(36.6), new BloodPressure(120, 80)));
        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);
        //имитируем поведение bloodPressure  нормальное давление
        BloodPressure bloodPressure = new BloodPressure(120, 80);
        //запускаем метод checkBloodPressure
        msi.checkBloodPressure(patientId, bloodPressure);
        //проверяем что метод  не будет вызван
        verify(sendAlertService, never()).send(anyString());
    }

    @Test
    //проверяем метод при нормальной температуре пациента
    public void checkTemperature_NormalTemperature_Test() {
        //Задаем параметр id
        String patientId = "15";
        //создаем пациента с нормальными параметрами
        PatientInfo patientInfo = new PatientInfo("John", "Do", LocalDate.now()
                , new HealthInfo(BigDecimal.valueOf(36.6), new BloodPressure(120, 80)));
        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);
        //эмитируем нормальную температуру
        BigDecimal bigDecimal = BigDecimal.valueOf(36.6);
        //передаем в метод в качестве параметра нормальную температуру
        msi.checkTemperature(patientId, bigDecimal);
        //проверяем что метод  не будет вызван
        verify(sendAlertService, never());
    }

    @Test
    //проверяем метод при нормальной температуре пациента
    public void checkTemperature_HihgTemperature_Test() {
        //Задаем параметр id
        String patientId = "15";
        //создаем пациента с нормальными параметрами
        PatientInfo patientInfo = new PatientInfo("John", "Do", LocalDate.now()
                , new HealthInfo(BigDecimal.valueOf(36.6), new BloodPressure(120, 80)));
        when(patientInfoRepository.getById(patientId)).thenReturn(patientInfo);
        //эмитируем повышенную температуру
        BigDecimal highTemperature = BigDecimal.valueOf(38.5);
        //передаем в метод в качестве параметра повышенную температуру
        msi.checkTemperature(patientId, highTemperature);
        //проверяем что метод был вызван 1 раз
        verify(sendAlertService, times(1));
    }
}