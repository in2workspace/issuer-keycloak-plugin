package es.in2.keycloak.model.walt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fiware.keycloak.oidcvc.model.DisplayObjectVO;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class IssuerDisplay {

	private List<DisplayObjectVO> display = new ArrayList<>();
}
