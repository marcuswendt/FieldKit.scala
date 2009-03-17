package field.kit.test.misc;

import java.util.ArrayList;

public class ArraysVSArrayBuffer {

	public static void main(String[] args) {
		info("-------- pre warmup --------");
		new ArraysVSArrayBuffer();

		info("-------- warmup --------");
		warmUp = true;
		for (int i = 0; i < 10000; i++)
			new ArraysVSArrayBuffer();
		warmUp = false;

		info("-------- running the test --------");
		new ArraysVSArrayBuffer();
	}

	static boolean warmUp = false;

	static public void info(String msg) {
		if (!warmUp)
			System.out.println(msg);
	}

	public ArraysVSArrayBuffer() {
		int iterations = 10000;
		float dt = 1.0f;

		// Array
		PrimitiveAgent[] ar = new PrimitiveAgent[iterations];

		start("array for-loop fill");
		for (int i = 0; i < ar.length; i++) {
			ar[i] = new PrimitiveAgent(i);
		}
		stop();

		start("array for-loop update with arg");
		for (int i = 0; i < ar.length; i++) {
			ar[i].update(dt);
		}
		stop();

		start("array for-loop update no arg");
		for (int i = 0; i < ar.length; i++) {
			ar[i].update();
		}
		stop();

		start("array for-loop update no arg unbounded");
		try {
			for (int i = 0;; i++) {
				ar[i].update();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		stop();

		// ArrayList
		ArrayList<PrimitiveAgent> al = new ArrayList<PrimitiveAgent>();

		start("ArrayList for-loop fill");
		for (int i = 0; i < iterations; i++) {
			al.add(new PrimitiveAgent(i));
		}
		stop();

		start("ArrayList for-loop update with arg");
		for (int i = 0; i < al.size(); i++) {
			al.get(i).update(dt);
		}
		stop();

		start("ArrayList for-loop update no arg");
		for (int i = 0; i < al.size(); i++) {
			al.get(i).update();
		}
		stop();
	}

	private long last = System.nanoTime();
	private String testName;

	public void start(String testName) {
		this.testName = testName;
		last = System.nanoTime();
	}

	public void stop() {
		double dur = (System.nanoTime() - last) / 1000000.0;
		info(testName + ": " + dur + "ms");
	}

	class PrimitiveAgent {
		public PrimitiveAgent(int i) {
		}

		public void update() {
		}

		public float update(float dt) {
			return dt + 1.0f;
		}
	}
}
