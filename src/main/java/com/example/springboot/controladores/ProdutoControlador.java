package com.example.springboot.controladores;

import com.example.springboot.dtos.ProdutoRecordDto;
import com.example.springboot.models.ProdutoModel;
import com.example.springboot.repositores.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProdutoControlador {

    @Autowired
    ProdutoRepository produtoRepository;

    @PostMapping("/nomeProduto")
    public ResponseEntity<ProdutoModel>saveNomeProduto(@RequestBody @Valid ProdutoRecordDto produtoRecordDto){
        var produtoModel = new ProdutoModel();
        BeanUtils.copyProperties(produtoRecordDto,produtoModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produtoModel));
    }

    //com hateoas
    @GetMapping("/nomeProduto")
    public ResponseEntity<List<ProdutoModel>>getAllNomeProduto(){
        List<ProdutoModel> listaProduto = produtoRepository.findAll();
        if(!listaProduto.isEmpty()){
            for(ProdutoModel produto: listaProduto){
                UUID id = produto.getIdProduto();
                produto.add(linkTo(methodOn(ProdutoControlador.class).getOneNomeProduto(id)).withSelfRel());
                }
        }
        return  ResponseEntity.status(HttpStatus.OK).body(produtoRepository.findAll());
    }


    @GetMapping("/nomeProduto/{id}")
    public ResponseEntity<Object>getOneNomeProduto(@PathVariable(value = "id")UUID id){
      Optional<ProdutoModel>nomeProduto0=produtoRepository.findById(id);
        if(nomeProduto0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
       }
        nomeProduto0.get().add(linkTo(methodOn(ProdutoControlador.class).getAllNomeProduto()).withRel("Produto Lista"));
       return ResponseEntity.status(HttpStatus.OK).body(nomeProduto0.get());
  }


   // @GetMapping("/nomeProduto")
   // public ResponseEntity<List<ProdutoModel>>getAllNomeProduto(){
     //   return  ResponseEntity.status(HttpStatus.OK).body(produtoRepository.findAll());
    //}

//    @GetMapping("/nomeProduto/{id}")
//    public ResponseEntity<Object>getOneNomeProduto(@PathVariable(value = "id")UUID id){
//        Optional<ProdutoModel>nomeProduto0=produtoRepository.findById(id);
//        if(nomeProduto0.isEmpty()){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(nomeProduto0.get());
//    }

    @PutMapping("/nomeProduto/{id}")
    public ResponseEntity<Object> updateNomeProduto(@PathVariable(value = "id")UUID id, @RequestBody @Valid ProdutoRecordDto produtoRecordDto){
        Optional<ProdutoModel> produto0 = produtoRepository.findById(id);
        if(produto0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não existe");
        }
        var produtoModel = produto0.get();
        BeanUtils.copyProperties(produtoRecordDto,produtoModel);
        return ResponseEntity.status(HttpStatus.OK).body((produtoRepository.save(produtoModel)));
    }

    @DeleteMapping("/nomeProduto/{id}")
    public ResponseEntity<Object> deleteProduto(@PathVariable(value = "id")UUID id){
        Optional<ProdutoModel> produto0 = produtoRepository.findById(id);
        if(produto0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não existe");
        }
        produtoRepository.delete(produto0.get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto excluido com sucesso");
    }


}
