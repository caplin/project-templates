import time
import caplin.datasrc as ds


class _ContainerProvider(ds.dataprovider.DataProvider):
    def __init__(self):
        self.publisher = None
        self.NUM_CONSTITUENTS = 50

    def initialise(self):
        self.publisher = ds.dataprovider.create_active_publisher(
            ds.namespace.create_prefix_namespace("/TEMPLATE/PYTHON/CONTAINER/ALL"), self)

    def receive_request(self, peer, subject):
        self.logger.info("Got request for {} from peer {}".format(subject, peer))

        container = ds.messaging.Container(subject,
                                           ds.messaging.DsData.Flags.F_IMAGE | ds.messaging.DsData.Flags.F_CONSTITUENT_NOAUTH)

        for i in range(0, self.NUM_CONSTITUENTS):
            container.add("/TEMPLATE/PYTHON/CONTAINERROW_{}".format(i))

        self.publisher.publish_response(container)

    def receive_discard(self, peer, subject):
        self.logger.info("Got discard for {} from peer {}".format(subject, peer))


class _ContainerRowProvider(ds.dataprovider.DataProvider):
    def __init__(self):
        self.publisher = None

    def initialise(self):
        self.publisher = ds.dataprovider.create_active_publisher(
            ds.namespace.create_prefix_namespace("/TEMPLATE/PYTHON/CONTAINERROW_"), self)

    def receive_request(self, peer, subject):
        self.logger.info("Got request for {} from peer {}".format(subject, peer))

        record = ds.messaging.Record(subject, ds.messaging.Record.Type.GENERIC, ds.messaging.DsData.Flags.F_IMAGE)
        record.add("currentTime", time.clock_gettime(time.CLOCK_REALTIME))
        self.publisher.publish_response(record)

    def receive_discard(self, peer, subject):
        self.logger.info("Got discard for {} from peer {}".format(subject, peer))


class ContainerTemplateProvider:
    def __init__(self):
        self.publisher = None
        self.logger = ds.get_logger()

    def initialise(self):
        self.logger.debug("Initialising providers")
        _ContainerProvider().initialise()
        _ContainerRowProvider().initialise()
        self.logger.debug("All providers initialised")
