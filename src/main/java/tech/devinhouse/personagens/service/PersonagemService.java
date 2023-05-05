package tech.devinhouse.personagens.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.devinhouse.personagens.exception.RegistroExistenteException;
import tech.devinhouse.personagens.exception.RegistroNaoEncontradoException;
import tech.devinhouse.personagens.model.Personagem;
import tech.devinhouse.personagens.repository.PersonagemRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PersonagemService {

    @Autowired
    private PersonagemRepository repo;

    public Personagem inserir(Personagem personagem) {
        boolean isCPFjaCadastrado = repo.existsPersonagemByCpf(personagem.getCpf());
        if (isCPFjaCadastrado) {
            log.warn("Solicitacao de cadastro de personagem com CPF já existente: {}", personagem.getCpf());
            throw new RegistroExistenteException();
        }
        personagem = repo.save(personagem);
        log.debug("Criado registro com id {}", personagem.getId());
        return personagem;
    }

    public List<Personagem> consultar() {
        List<Personagem> personagens = repo.findAll();
        return personagens;
    }

    public Personagem consultar(Long id) {
        Optional<Personagem> personagemOpt = repo.findById(id);
        log.trace("Consultado personagem pelo id {}", id);
        return personagemOpt.orElseThrow(RegistroNaoEncontradoException::new);
    }

    public Personagem consultarPor(Long cpf) {
        Optional<Personagem> personagemOpt = repo.findByCpf(cpf);
        return personagemOpt.orElseThrow(RegistroNaoEncontradoException::new);
    }

    public List<Personagem> inserir(List<Personagem> personagens) {
        return personagens.stream()
                .map(p -> inserir(p))
                .toList();
    }

    public Personagem alterar(Personagem alterado) {
        var personagem = repo.findById(alterado.getId())
                .orElseThrow(RegistroNaoEncontradoException::new);
        log.debug("Dados originais: {}", personagem);
        personagem.setNome(alterado.getNome());
        personagem.setDataNascimento(alterado.getDataNascimento());
        personagem.setSerie(alterado.getSerie());
        log.debug("Dados alterados: {}", personagem);
        personagem = repo.save(personagem);
        return personagem;
    }

    public void excluir(Long id) {
        var personagem = repo.findById(id)
                .orElseThrow(RegistroNaoEncontradoException::new);
        log.debug("Registro excluído: {}", personagem);
        repo.delete(personagem);
    }

    public String consultarNome(Long id) {
        Personagem personagem = this.consultar(id);
        return personagem.getNome();
    }

    public Integer consultarIdade(Long id) {
        Personagem personagem = this.consultar(id);
        int idade = (int) Duration.between(personagem.getDataNascimento().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays() / 365;
        return idade;
    }

}
