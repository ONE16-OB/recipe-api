package be.one16.cerbainterview.api;

import be.one16.cerbainterview.AbstractBaseTest;
import be.one16.cerbainterview.core.IngredientService;
import be.one16.cerbainterview.db.repositories.IngredientRepository;
import be.one16.cerbainterview.domain.Ingredient;
import be.one16.cerbainterview.model.IngredientDTO;
import be.one16.cerbainterview.model.IngredientDataDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class IngredientControllerTest extends AbstractBaseTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private IngredientService ingredientService;
    @Autowired
    private IngredientRepository ingredientRepository;

    @NotNull
    private ResultActions performDefaultCreate() throws Exception {
        return mockMvc.perform(post("/ingredients")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        Ingredient.builder()
                        .name("Test Ingredient")
                        .quantity("niks")
                                .build()
                )));
    }

    @Test
    void testCreateIngredient() throws Exception {
        performDefaultCreate()
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Ingredient"));
    }

    @BeforeEach
    void setUp() {
        ingredientRepository.deleteAll();
    }

    @Test
    void testGetIngredientByUUID() throws Exception {
        // First, create a new ingredient
        MvcResult mvcResult = performDefaultCreate().andReturn();
        // Extract the UUID from the created ingredient response
        String contentAsString = mvcResult.getResponse().getContentAsString();
        UUID ingredientUUID = objectMapper.readValue(contentAsString, IngredientDTO.class).getUuid();
        // Then, test the getIngredientByUUID endpoint using the UUID
        mockMvc.perform(MockMvcRequestBuilders.get("/ingredients/{uuid}", ingredientUUID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Ingredient"));
    }

    @Test
    void testUpdateIngredientByUUID() throws Exception {
        // First, create a new ingredient
        MvcResult mvcResult = performDefaultCreate().andReturn();
        // Extract the UUID from the created ingredient response
        UUID ingredientUUID = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), IngredientDTO.class).getUuid();
        // Then, update the ingredient with a new name
        mockMvc.perform(MockMvcRequestBuilders.put("/ingredients/{uuid}", ingredientUUID)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Ingredient.builder().name("Updated Ingredient").quantity("niks").build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Ingredient"));
    }

    @Test
    void testGetAllIngredientsPaged() throws Exception {

        List.of("Ingredient 1","Ingredient 2", "Ingredient 3").forEach(name -> {
            IngredientDataDTO ingredientDataDTO = new IngredientDataDTO();
            ingredientDataDTO.setName(name);
            ingredientService.createIngredient(ingredientDataDTO);
        });

        // Perform GET request to retrieve all ingredients
        mockMvc.perform(MockMvcRequestBuilders.get("/ingredients")
                        .param("page", "0")
                        .param("size", "3"))
                // Check status code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Check if content is an array
                .andExpect(jsonPath("$.content").isArray())
                // Check if all three ingredients are present in the response
                .andExpect(jsonPath("$.content[0].name").value("Ingredient 1"))
                .andExpect(jsonPath("$.content[1].name").value("Ingredient 2"))
                .andExpect(jsonPath("$.content[2].name").value("Ingredient 3"));
    }

    @Test
    void testDeleteIngredientByUUID() throws Exception {
        // First, create a new ingredient
        MvcResult mvcResult = performDefaultCreate().andReturn();
        // Extract the UUID from the created ingredient response
        UUID ingredientUUID = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), IngredientDTO.class).getUuid();
        // Then, delete the ingredient using its UUID
        mockMvc.perform(MockMvcRequestBuilders.delete("/ingredients/{uuid}", ingredientUUID))
                .andExpect(status().isNoContent());
    }
}

