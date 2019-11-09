package br.furb.aes.key;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class KeySchedule {

    @Delegate
    private List<RoundKey> keySchedule = new ArrayList<RoundKey>();


}
