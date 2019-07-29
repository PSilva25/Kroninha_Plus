package com.xetelas.nova.Objects;

public class Caronas implements Comparable<Caronas> {

    String Id, id_post, origem, destino, data, hora, coment, nome, tell, link;

    public Caronas() {}

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId_post() {
        return id_post;
    }

    public void setId_post(String id_post) {
        this.id_post = id_post;
    }

    public String getTell() {
        return tell;
    }

    public void setTell(String tell) {
        this.tell = tell;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getOrigem() {
        return origem;
    }

    public String getId() { return Id; }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public String setId(String Id) { return Id;  }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getComent() {
        return coment;
    }

    public void setComent(String coment) {
        this.coment = coment;
    }

    @Override
    public int compareTo(Caronas carona) {
        int esse, oto;
        try {
            esse = Integer.parseInt(this.id_post);
        } catch(NumberFormatException e) {
            esse = 0;
        }
        try {
            oto = Integer.parseInt(carona.id_post);
        } catch(NumberFormatException e) {
            oto = 0;
        }

        if (esse < oto) {
            return -1;
        }
        if (esse > oto) {
            return 1;
        }
        return 0;
    }
}
