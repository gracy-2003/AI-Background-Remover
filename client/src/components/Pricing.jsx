import { plans } from "../assets/assets";

const Pricing = () => {
  return (
    <div className="py-10 md:px-20 lg:px-20">
      <div className="container mx-auto px-4">

        {/* Section title */}
        <div className="mb-12 text-center">
          <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-6">
            Choose your perfect package
          </h2>

          <p className="max-w-2xl mx-auto text-gray-600">
            Select from our carefully curated photography packages designed
            to meet your specific needs and budget.
          </p>
        </div>

        {/* Section body */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">

          {plans.map((plan) => (
            <div
              key={plan.id}
              className={`relative pt-6 pb-6 bg-black text-white ${
                plan.popular
                  ? "backdrop-blur-lg rounded-2xl shadow-xl"
                  : "border border-gray-300 rounded-xl"
              } hover:transform hover:-translate-y-2 transition-all duration-300`}
            >

              {/* Most Popular Badge */}
              {plan.popular && (
                <div className="absolute -top-4 left-1/2 -translate-x-1/2 rounded-full bg-purple-600 px-3 py-1 text-white text-sm font-semibold">
                  Most Popular
                </div>
              )}

              {/* Plan name */}
              <div className="text-center p-6">
                <h3 className="text-2xl font-bold">
                  {plan.name}
                </h3>

                <div className="mt-4">
                  <span className="text-4xl text-violet-400 font-bold">
                    ₹{plan.price}
                  </span>
                </div>
              </div>

              {/* Plan details */}
              <div className="px-6 pb-6">
                <ul className="space-y-4 text-center">
                  <li>{plan.credits}</li>
                  <li>{plan.description}</li>
                </ul>

                <button className="w-full py-3 px-6 mt-6 text-center text-white font-semibold rounded-full bg-gradient-to-r from-purple-500 to-indigo-500 shadow-lg hover:scale-105 transition-transform duration-300">
                  Choose plan
                </button>
              </div>

            </div>
          ))}

        </div>

      </div>
    </div>
  );
};

export default Pricing;