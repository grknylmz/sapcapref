using {ProductService} from '../srv/services.cds';

annotate ProductService.Products with @Common.SemanticKey: [productID];