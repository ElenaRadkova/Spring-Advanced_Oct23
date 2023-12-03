package bg.softuni.hateoas.web;

import bg.softuni.hateoas.model.dto.OrderDTO;
import bg.softuni.hateoas.model.dto.StudentDTO;
import bg.softuni.hateoas.model.entity.OrderEntity;
import bg.softuni.hateoas.model.entity.StudentEntity;
import bg.softuni.hateoas.model.mapping.StudentMapper;
import bg.softuni.hateoas.repository.StudentRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/students")
@RestController
public class StudentsController {


    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;


    public StudentsController(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<StudentDTO>>> getStudents(){
             List<EntityModel<StudentDTO>> allStudents = studentRepository
                     .findAll()
                     .stream()
                     .map(studentMapper::mapEntityDTO)
                     .map(dto -> EntityModel.of(dto, createStudentsLinks(dto)))
                     .collect(Collectors.toList());

             return ResponseEntity.ok(CollectionModel.of(allStudents));
    }
    @GetMapping("/{id}/orders")
    public ResponseEntity<CollectionModel<EntityModel<OrderDTO>>> getOrders(@PathVariable("id") Long id) {
        StudentEntity student = studentRepository
                .findById(id)
                .orElseThrow();

        List<EntityModel<OrderDTO>> orders = student
                .getOrders()
                .stream()
                .map(this::map)
                .map(EntityModel::of)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(orders));
    }
    private OrderDTO map(OrderEntity order) {
        return new OrderDTO()
                .setId(order.getId())
                .setStudentId(order.getStudent().getId())
                .setCourseId(order.getCourse().getId());
    }


    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<StudentDTO>> getStudentsById(@PathVariable("id") Long id){
        StudentDTO student = studentRepository
                .findById(id)
                .map(studentMapper::mapEntityDTO)
                .orElseThrow();

        return ResponseEntity.ok(EntityModel.of(student, createStudentsLinks(student)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<StudentDTO>> update(@PathVariable Long id, StudentDTO studentDTO) {
        return ResponseEntity.ok().build();
    }

    private Link[] createStudentsLinks(StudentDTO studentDTO) {
        List<Link> result = new ArrayList<>();

        Link selfLink = linkTo(methodOn(StudentsController.class)
                .getStudentsById(studentDTO.getId()))
                .withSelfRel();
        result.add(selfLink);

        Link updateLink = linkTo(methodOn(StudentsController.class)
                .update(studentDTO.getId(), studentDTO))
                .withRel("update");
        result.add(updateLink);

        Link orderLink = linkTo(methodOn(StudentsController.class)
                .getOrders(studentDTO.getId()))
                .withRel("orders");
        result.add(orderLink);

        return result.toArray(new Link[0]);
    }

}
