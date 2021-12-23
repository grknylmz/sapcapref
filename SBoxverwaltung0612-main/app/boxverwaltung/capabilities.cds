using {BoxService} from '../../srv/boxservice';

annotate BoxService.Box with @odata.draft.enabled;
annotate BoxService.Box with @Common.SemanticKey: [BoxID];
