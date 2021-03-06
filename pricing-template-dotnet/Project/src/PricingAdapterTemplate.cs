using Caplin.DataSource;
using Caplin.Logging;

using System.Threading;

using Caplin.Template;

namespace Caplin.Template
{
    public class PricingAdapterTemplate
    {
        private IDataSource dataSource;
        private static ManualResetEvent _quitEvent = new ManualResetEvent(false);

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

            IDataSource dataSource = new Caplin.DataSource.DataSource("TemplatePricingAdapter.conf", args);
            var templateAdapter = new PricingAdapterTemplate(dataSource);

            templateAdapter.Initialise();

            dataSource.Start();

            // Now we have to block main thread - DataSource.NET runs in a background thread
            _quitEvent.WaitOne();
        }
    }

}
