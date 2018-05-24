using Caplin.DataSource;
using Caplin.Logging;

using System.Threading;

using Caplin.Template;

namespace Caplin.Template
{
    public class PricingAdapterTemplate
    {
        private IDataSource dataSource;

        public PricingAdapterTemplate(IDataSource dataSource)
        {
            this.dataSource = dataSource;
        }

        public void Initialise()
        {
            var pricingTemplateDataProvider = new PricingTemplateDataProvider(dataSource);
            //ContainerTemplateDataProvider containerTemplateDataProvider = new ContainerTemplateDataProvider(dataSource);
            var channelTemplate = new ChannelTemplate(dataSource);
            //Type2DataProvider type2DataProvider = new Type2DataProvider(dataSource);

            pricingTemplateDataProvider.Initialise();
            //containerTemplateDataProvider.initialise();
            channelTemplate.Initialise();
            //type2DataProvider.initialise();
        }

        static void Main(string[] args)
        {

            ILogger logger = new ConsoleLogger();
            IDataSource dataSource = new Caplin.DataSource.DataSource("demosource.conf", args, logger);
            var templateAdapter = new PricingAdapterTemplate(dataSource);

            templateAdapter.Initialise();

            dataSource.Start();

            // Now we have to loop forever - DataSource.NET runs in a background thread
            while (true)
            {
                Thread.Sleep(1000);
            }
        }
    }

}
