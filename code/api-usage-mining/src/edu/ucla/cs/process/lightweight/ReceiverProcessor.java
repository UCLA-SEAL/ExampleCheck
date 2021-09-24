package edu.ucla.cs.process.lightweight;

import com.google.common.collect.HashMultiset;

import edu.ucla.cs.model.Method;
import edu.ucla.cs.model.Receiver;

public class ReceiverProcessor extends ProcessStrategy{

	@Override
	void process(String line) {
		Method m = getMethodInstance(line);
		buildReceiverMap(m, line);
	}

	private void buildReceiverMap(Method m, String line) {
		String s = line.substring(line.indexOf("] =") + 3).trim();
		String[] ss = s.split("@@");
		for(int i = 0; i < ss.length; i++){
			String call = ss[i].split("->")[0];
			String vars = ss[i].split("->")[1];
			
			HashMultiset<Receiver> rs = HashMultiset.create();
			
			String[] arr = vars.split(";;;");
			for(int j = 0; j < arr.length; j++) {
				String receiver = arr[j];
				Receiver rcv = new Receiver(receiver, call);
				rs.add(rcv);
				HashMultiset<Receiver> rev_rs;
				if(m.rev_receivers.containsKey(receiver)) {
					rev_rs = m.rev_receivers.get(receiver);
				} else {
					rev_rs = HashMultiset.create();
				}
				rev_rs.add(rcv);
				m.rev_receivers.put(receiver, rev_rs);
			}
			
			m.receivers.put(call, rs);
		}
	}
}
