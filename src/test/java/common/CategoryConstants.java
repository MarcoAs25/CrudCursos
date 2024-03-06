package common;

import com.marcoas.crudCursos.dto.CategoryDTO;
import com.marcoas.crudCursos.model.Category;

public class CategoryConstants {
    public static final CategoryDTO CATEGORYDTO = new CategoryDTO("Tecnologia da Informação");
    public static final CategoryDTO INVALIDCATEGORYDTO1 = new CategoryDTO(null);
    public static final CategoryDTO INVALIDCATEGORYDTO2 = new CategoryDTO("");
    public static final CategoryDTO INVALIDCATEGORYDTO3 = new CategoryDTO("    ");
    public static final Category CATEGORYENTITY = new Category(1l,"Tecnologia da Informação");
    public static final Category CATEGORYENTITYTOSAVE = new Category(null,"Tecnologia da Informação");

}
