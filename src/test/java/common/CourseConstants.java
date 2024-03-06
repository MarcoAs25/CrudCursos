package common;

import com.marcoas.crudCursos.dto.CourseDTO;
import com.marcoas.crudCursos.model.Course;
public class CourseConstants {
    public static final CourseDTO COURSEDTO = new CourseDTO("Angular", 1l);
    public static final CourseDTO INVALIDCOURSEDTO1 = new CourseDTO(" ", 1l);
    public static final CourseDTO INVALIDCOURSEDTO2 = new CourseDTO(null, 1l);
    public static final CourseDTO INVALIDCOURSEDTO3 = new CourseDTO("Angular", null);

    public static final Course COURSEENTITY = new Course(1l,"Angular", CategoryConstants.CATEGORYENTITY);

}
