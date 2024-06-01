package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastra(@RequestBody @Valid DadosCadastroMadico dados, UriComponentsBuilder uriBuilder){
        var medico = new Medico(dados);
        repository.save(medico);

        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));

    }
//    lista carregatodos registo
//    @GetMapping
//    public Page<DadosListagemMedico>listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){
//        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
//
//    }

// carrega so registo ativo
    @GetMapping
    public ResponseEntity <Page<DadosListagemMedico>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){
       var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
       return ResponseEntity.ok(page);
    }
    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMadico dados){
        var medico = repository.getReferenceById(dados.id());
        medico.atuaLizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excuir(@PathVariable Long id){
        var medico =repository.getReferenceById(id);
        medico.excuir();
        return ResponseEntity.noContent().build();

    }
    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var medico =repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));

    }

}
