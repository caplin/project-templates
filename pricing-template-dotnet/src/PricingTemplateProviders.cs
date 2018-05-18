using System;
using System.Timers;
using System.Collections.Generic;

using Caplin.Logging;
using Caplin.DataSource;
using Caplin.DataSource.Namespace;
using Caplin.DataSource.Publisher;
using Caplin.DataSource.Messaging.Record;
using Caplin.DataSource.Channel;

namespace Caplin.Template
{
    public class PricingTemplateDataProvider : IDataProvider
    {
        private IDataSource dataSource;
        private IActivePublisher publisher;
        private Dictionary<string, Timer> activeSubscriptions = new Dictionary<string, Timer>();

        public PricingTemplateDataProvider(IDataSource dataSource)
        {
            this.dataSource = dataSource;
        }

        public void Initialise()
        {
            publisher = dataSource.CreateActivePublisher(new PrefixNamespace("/TEMPLATE/DOTNET/PRICING"), this);
        }

        public void ReceiveRequest(IRequestEvent requestEvent)
        {
            var split = requestEvent.Subject.Split('/');

            if ( split.Length == 4 && split[3].Length > 0) {
                var timer = new Timer();
                var priceGenerator = new PriceGenerator(requestEvent.Subject, publisher, dataSource.Logger);
                timer.Elapsed += (sender, e) => priceGenerator.Emit();
                timer.Interval = 2000;
                timer.Enabled = true;

                activeSubscriptions.Add(requestEvent.Subject, timer);
            } else {
                var errorEvent = publisher.MessageFactory.CreateSubjectErrorEvent(requestEvent.Subject, SubjectError.NotFound);
                publisher.PublishSubjectErrorEvent(errorEvent);
            }

        }

        public void ReceiveDiscard(IDiscardEvent discardEvent)
        {
        }

        class PriceGenerator
        {
            private readonly string subject;
            private readonly ILogger logger;
            private readonly IActivePublisher publisher;

            public PriceGenerator(string subject, IActivePublisher publisher, ILogger logger)
            {
                this.subject = subject;
                this.logger = logger;
                this.publisher = publisher;
            }

            public void Emit() 
            {
                IRecordMessage genericMessage = publisher.MessageFactory.CreateGenericMessage(subject);

                genericMessage.SetField("BestBid", 10.ToString());
                genericMessage.SetField("BestAsk", 10.ToString());

                publisher.PublishInitialMessage(genericMessage);
            }
        }
    }

    public class ChannelTemplate : IChannelListener
    {
        private const string OPERATION_FIELD = "operation";
        private const string DESCRIPTION_FIELD = "description";

        private readonly IDataSource dataSource;
        private readonly Dictionary<string, IChannel> newChannels = new Dictionary<string, IChannel>();
        private Timer timer = new Timer();


        public ChannelTemplate(IDataSource dataSource)
        {
            this.dataSource = dataSource;
            timer.Interval = 1000;
            timer.Elapsed += (sender, e) => {
                foreach (KeyValuePair<string, IChannel> pair in newChannels) {
                        IRecordMessage message = pair.Value.CreateRecordMessage(pair.Key);
                        message.SetField(OPERATION_FIELD, "Hello");
                        message.SetField(DESCRIPTION_FIELD, "Please send a contrib with the field `operation` set to the value `Ping` to this channel");
                        message.Image = true;
                        pair.Value.SendMessage(message);

                        newChannels.Remove(pair.Key);
                }
            };
            timer.Enabled = true;
        }

        public void Initialise() 
        {
            dataSource.AddGenericChannelListener(new PrefixNamespace("/TEMPLATE/DOTNET/CHANNEL"), this);
        }

        public bool ChannelOpened(IChannel channel)
        {
            newChannels.Add(channel.Subject, channel);
            return true;
        }

        public void ChannelClosed(IChannel channel) 
        {
        }

        public void MessageReceived(IChannel channel, IRecordMessage recordMessage)
        {
            newChannels.Remove(channel.Subject); //Take channel out of newChannels map so that we don't send the Hello
            IRecordMessage message = channel.CreateRecordMessage(channel.Subject);

            if (recordMessage[OPERATION_FIELD].Equals("Ping")) {
                message.SetField(OPERATION_FIELD, "Pong");
                message.SetField(DESCRIPTION_FIELD, "Pong");
            } else {
                message.SetField(OPERATION_FIELD, "Error");
                message.SetField(DESCRIPTION_FIELD, "Please send a contrib with the field `operation` set to the value `Ping` to this channel");
            }

            channel.SendMessage(message);
        }
    }
}
