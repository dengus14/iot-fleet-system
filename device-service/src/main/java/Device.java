import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "device")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double engineTemp;
    @Column(nullable = false)
    private DeviceType deviceType;

    @Column
    private UndirectedNode startLocation;

    @Column
    private Double speed;
}
