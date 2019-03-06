import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IConfig } from 'app/shared/model/config.model';
import { AccountService } from 'app/core';
import { ConfigService } from './config.service';

@Component({
    selector: 'jhi-config',
    templateUrl: './config.component.html'
})
export class ConfigComponent implements OnInit, OnDestroy {
    configs: IConfig[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected configService: ConfigService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.configService
                .search({
                    query: this.currentSearch
                })
                .subscribe(
                    (res: HttpResponse<IConfig[]>) => (this.configs = res.body),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.configService.query().subscribe(
            (res: HttpResponse<IConfig[]>) => {
                this.configs = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInConfigs();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IConfig) {
        return item.id;
    }

    registerChangeInConfigs() {
        this.eventSubscriber = this.eventManager.subscribe('configListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
