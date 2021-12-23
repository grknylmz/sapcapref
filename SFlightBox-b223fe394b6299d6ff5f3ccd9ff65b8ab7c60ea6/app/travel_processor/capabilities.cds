using {BoxService} from '../../srv/travel-service';

annotate BoxService.Box with @odata.draft.enabled;
annotate BoxService.Box with @Common.SemanticKey: [BoxID];
