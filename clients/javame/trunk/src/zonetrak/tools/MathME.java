package zonetrak.tools;

public class MathME
{
	final static public double SQRT3 = 1.732050807568877294;
	final static public double LOG10 = 2.302585092994045684;
	final static public double LOGdiv2 = -0.6931471805599453094;

	private static double _log(double x) {
		if (!(x > 0.))
		{
			return Double.NaN;
		}

		double f = 0.0;

		int appendix = 0;
		while (x > 0.0 && x <= 1.0)
		{
			x *= 2.0;
			appendix++;
		}

		x /= 2.0;
		appendix--;

		final double y1 = x - 1.;
		double y2 = x + 1.;
		final double y = y1 / y2;

		double k = y;
		y2 = k * y;

		for (long i = 1; i < 50; i += 2)
		{
			f += k / i;
			k *= y2;
		}

		f *= 2.0;
		for (int i = 0; i < appendix; i++)
		{
			f += LOGdiv2;
		}

		return f;
	}

	public static double acos(double x) {
		final double f = asin(x);

		if (f == Double.NaN)
		{
			return f;
		}

		return Math.PI / 2 - f;
	}

	public static double asin(double x) {
		if (x < -1. || x > 1.)
		{
			return Double.NaN;
		}

		if (x == -1.)
		{
			return -Math.PI / 2;
		}

		if (x == 1)
		{
			return Math.PI / 2;
		}

		return atan(x / Math.sqrt(1 - x * x));
	}

	public static double atan(double x) {
		boolean signChange = false;
		boolean invert = false;
		int sp = 0;
		double x2, a;

		if (x < 0.)
		{
			x = -x;
			signChange = true;
		}

		if (x > 1.)
		{
			x = 1 / x;
			invert = true;
		}

		while (x > Math.PI / 12)
		{
			sp++;
			a = x + SQRT3;
			a = 1 / a;
			x = x * SQRT3;
			x = x - 1;
			x = x * a;
		}

		x2 = x * x;
		a = x2 + 1.4087812;
		a = 0.55913709 / a;
		a = a + 0.60310579;
		a = a - x2 * 0.05160454;
		a = a * x;

		while (sp > 0)
		{
			a = a + Math.PI / 6;
			sp--;
		}

		if (invert)
		{
			a = Math.PI / 2 - a;
		}

		if (signChange)
		{
			a = -a;
		}

		return a;
	}

	public static double atan2(double y, double x) {
		if (y == 0. && x == 0.)
		{
			return 0.;
		}

		if (x > 0.)
		{
			return atan(y / x);
		}

		if (x < 0.)
		{
			if (y < 0.)
			{
				return -(Math.PI - atan(y / x));
			}
			else
			{
				return Math.PI - atan(-y / x);
			}
		}

		if (y < 0.)
		{
			return -Math.PI / 2.;
		}
		else
		{
			return Math.PI / 2.;
		}
	}

	public static double exp(double x) {
		if (x == 0.)
		{
			return 1.;
		}

		double f = 1;
		final long d = 1;
		double k;
		final boolean isless = x < 0.;
		if (isless)
		{
			x = -x;
		}
		k = x / d;

		for (long i = 2; i < 50; i++)
		{
			f = f + k;
			k = k * x / i;
		}

		if (isless)
		{
			return 1 / f;
		}
		else
		{
			return f;
		}
	}

	public static double log(double x) {
		if (!(x > 0.))
		{
			return Double.NaN;
		}

		if (x == 1.0)
		{
			return 0.0;
		}

		if (x > 1.)
		{
			x = 1 / x;
			return -_log(x);
		}

		return _log(x);
	}

	public static double log10(double x) {
		return log(x) / LOG10;
	}

	public static double pow(double x, double y) {
		if (y == 0.)
		{
			return 1.;
		}
		if (y == 1.)
		{
			return x;
		}
		if (x == 0.)
		{
			return 0.;
		}
		if (x == 1.)
		{
			return 1.;
		}

		final long l = (long) Math.floor(y);
		final boolean integerValue = y == l;

		if (integerValue)
		{
			boolean neg = false;
			if (y < 0.)
			{
				neg = true;
			}

			double result = x;
			for (long i = 1; i < (neg ? -l : l); i++)
			{
				result = result * x;
			}

			if (neg)
			{
				return 1. / result;
			}
			else
			{
				return result;
			}
		}
		else
		{
			if (x > 0.)
			{
				return exp(y * log(x));
			}
			else
			{
				return Double.NaN;
			}
		}
	}

	public static double cosh(double x) {
		return (exp(x) + exp(-x)) / 2.0;
	}

	public static double sinh(double x) {
		return (exp(x) - exp(-x)) / 2.0;
	}

	public static double tanh(double x) {
		return sinh(x) / cosh(x);
	}

	public static double sech(double x) {
		return 1. / cosh(x);
	}

	public static double cotanh(double x) {
		return cosh(x) / sinh(x);
	}

	public static int round(double a)
	{
		if (a >= 0)
		{
			return (int) Math.floor(a + 0.5d);
		}
		else
		{
			return (int) Math.floor(a - 0.5d);
		}
	}
}
