import random
import threading

import caplin.datasrc as ds
import time


def _update_generator(provider):
    while True:
        for sub in provider.active_subs:
            update = ds.messaging.Record(sub, ds.messaging.Record.Type.TYPE1)
            update.add("Field1", random.randint(0, 1000))
            provider.publisher.publish_to_subscribed_peers(update)

        time.sleep(1)


class PricingTemplateProvider(ds.dataprovider.DataProvider):
    def __init__(self):
        self.active_subs = set()
        self.generator_thread = None
        self.publisher = None
        self.logger = ds.get_logger()

    def initialise(self):
        self.publisher = ds.dataprovider.create_active_publisher(ds.namespace.create_prefix_namespace("/TEMPLATE/PYTHON/PRICING"), self)
        self.generator_thread = threading.Thread(target=_update_generator, name="pricing-generator", args=(self,))
        self.generator_thread.start()

    def receive_request(self, peer, subject):
        self.logger.info("Got request for {} from peer {}".format(subject, peer))

        parts = subject.split("/")

        if len(parts) is 4 and not parts[3].isEmpty():
            self.active_subs.add(subject)
        else:
            self.publisher.publish_subject_error(subject, ds.Publisher.SubjectErrorFlags.F_NOTFOUND)

    def receive_discard(self, peer, subject):
        self.logger.info("Got discard for {} from {}".format(subject, peer))
        self.active_subs.discard(subject)
