package med.voll.api.paciente;

public record DadosListagemPacient(Long id, String nome, String email, String cpf) {

    public DadosListagemPacient(Paciente paciente) {
        this(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getCpf());
    }
}
