import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UndirectedNode {



    private Integer id;
    private String locationName;
    private List<Integer> neighbors;
    private String description;


}
